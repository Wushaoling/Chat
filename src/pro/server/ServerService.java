package pro.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import pro.model.Info;
import pro.model.User;
import pro.util.Address;
import pro.util.DataBaseUtil;
import pro.util.DateFormatUtil;
import pro.util.EnumInfoType;
@SuppressWarnings("all")
/**
 * �ͻ��˼�������
 * 
 * @author wushaoling
 *
 */

public class ServerService {
	// �̳߳�
	ExecutorService es = Executors.newFixedThreadPool(1000);
	// �������������û������߳�
	private Vector<UserServiceThread> userThreads = new Vector<UserServiceThread>();

	private JTextArea jtextarea;
	private DefaultListModel listModel;

	public ServerService(JTextArea jtextarea, DefaultListModel listModel) {
		this.jtextarea = jtextarea;
		this.listModel = listModel;
		this.jtextarea.append("\n" + DataBaseUtil.connection());
	}

	public void startServer() {
		boolean flag = true;// ��Ͳ�ͻ����Ƿ����ӵı��
		try {
			ServerSocket server = new ServerSocket(Address.port);
			while (flag) {
				Socket client = server.accept();
				UserServiceThread ust = new UserServiceThread(client);

				userThreads.add(ust);
				es.execute(ust);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �û������߳�
	 * 
	 * @author wushaoling
	 *
	 */
	public class UserServiceThread implements Runnable {
		ObjectInputStream is = null;
		ObjectOutputStream out = null;
		private Socket client;
		private boolean flag = true;
		private User currUserInfo;

		public UserServiceThread(Socket client) {
			this.client = client;
			try {
				is = new ObjectInputStream(new BufferedInputStream(
						client.getInputStream()));
				out = new ObjectOutputStream(client.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (flag) {
				Info info = null;
				try {
					info = (Info) is.readObject();
					switch (info.getInfotype()) {
					case LOGIN: // ��¼��Ϣ
						// ���ػ�ӭ��Ϣ
						if (DataBaseUtil.checkLoginInfo(info.getFromUser(),info.getContent())) {
							jtextarea.append(jtextarea.getText() + "\n"
									+ "�û��� " + info.getFromUser() + "����.");
							Info loginInfo = new Info();
							loginInfo.setContent("��ӭ�㣬" + info.getFromUser());
							loginInfo.setFromUser("����Ա");
							loginInfo.setSendTime(DateFormatUtil
									.getTime(new Date()));
							loginInfo.setToUser(info.getFromUser());
							loginInfo.setInfotype(EnumInfoType.LOGIN_RESUALT);
							currUserInfo = new User();
							currUserInfo.setName(info.getFromUser());
							out.writeObject(loginInfo);
							out.flush();

							// ���·������û��б�
							listModel.addElement(info.getFromUser());

							StringBuffer sb = new StringBuffer();// �洢�û��б�

							// �������û����͸����û�����Ϣ
							for (UserServiceThread user : userThreads) {
								if (!info.getFromUser().equals(
										user.currUserInfo.getName())) {
									sb.append(user.currUserInfo.getName())
											.append(",");
									Info updateUserListInfo = new Info();
									updateUserListInfo.setContent(info
											.getFromUser());
									updateUserListInfo
											.setInfotype(EnumInfoType.ADD_USER);
									user.out.writeObject(updateUserListInfo);
									user.out.flush();
								}
							}

							// ��ǰ�û����������б�
							Info updateInfo = new Info();
							updateInfo.setContent(sb.toString());
							updateInfo.setInfotype(EnumInfoType.LOAD_USER);
							out.writeObject(updateInfo);
							out.flush();
						} else {
							Info loginInfo = new Info();
							loginInfo.setContent("false");
							loginInfo.setFromUser("����Ա");
							loginInfo.setSendTime(DateFormatUtil
									.getTime(new Date()));
							loginInfo.setToUser(info.getFromUser());
							loginInfo.setInfotype(EnumInfoType.LOGIN_RESUALT);
							out.writeObject(loginInfo);
							out.flush();
						}
						break;
					// ˽��
					case SEND_INFO:
						for (UserServiceThread user : userThreads) {
							if (user.currUserInfo.getName().equals(
									info.getToUser())) {
								user.out.writeObject(info);
								user.out.flush();
							}
						}
						break;
					// Ⱥ��
					case SEND_ALL:
						for (UserServiceThread user : userThreads) {
							if (!user.currUserInfo.getName().equals(
									info.getFromUser())) {
								info.setInfotype(EnumInfoType.SEND_INFO);
								user.out.writeObject(info);
								out.flush();
							}
						}
						break;

					// �û������˳�
					case DELETE_USER:
						exit(info);
						break;
						
					//��¼����˳�
					case QUIT:
						flag = false;
						userThreads.remove(this);
						break;
						
					//ע��
					case REGISTER:
						if((DataBaseUtil.checkRegisterInfo(info.getFromUser())) && (DataBaseUtil.saveInfo(info))){
							info.setContent("ע��ɹ�");
							info.setInfotype(EnumInfoType.REGISTER);
							out.writeObject(info);
							out.flush();
						}else{
							info.setContent("�û����Ѵ���");
							info.setInfotype(EnumInfoType.REGISTER);
							out.writeObject(info);
							out.flush();
						}
						userThreads.remove(this);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					try {
						exit(info);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}

		private void exit(Info info) throws IOException {
			for (UserServiceThread user : userThreads) {
				if (user.currUserInfo.getName().equals(info.getFromUser())) {
					user.flag = false;
					userThreads.remove(user);
					listModel.removeElement(info.getFromUser());
					continue;
				}
				info.setToUser(user.currUserInfo.getName());
				info.setContent(info.getFromUser());
				info.setInfotype(EnumInfoType.DELETE_USER);
				user.out.writeObject(info);
				user.out.flush();
			}
		}	
	}
}
