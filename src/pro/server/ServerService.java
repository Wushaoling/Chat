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
 * 客户端监听服务
 * 
 * @author wushaoling
 *
 */

public class ServerService {
	// 线程池
	ExecutorService es = Executors.newFixedThreadPool(1000);
	// 保存所有在线用户服务线程
	private Vector<UserServiceThread> userThreads = new Vector<UserServiceThread>();

	private JTextArea jtextarea;
	private DefaultListModel listModel;

	public ServerService(JTextArea jtextarea, DefaultListModel listModel) {
		this.jtextarea = jtextarea;
		this.listModel = listModel;
		this.jtextarea.append("\n" + DataBaseUtil.connection());
	}

	public void startServer() {
		boolean flag = true;// 箭筒客户端是否连接的标记
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
	 * 用户服务线程
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
					case LOGIN: // 登录消息
						// 返回欢迎消息
						if (DataBaseUtil.checkLoginInfo(info.getFromUser(),info.getContent())) {
							jtextarea.append(jtextarea.getText() + "\n"
									+ "用户： " + info.getFromUser() + "上线.");
							Info loginInfo = new Info();
							loginInfo.setContent("欢迎你，" + info.getFromUser());
							loginInfo.setFromUser("管理员");
							loginInfo.setSendTime(DateFormatUtil
									.getTime(new Date()));
							loginInfo.setToUser(info.getFromUser());
							loginInfo.setInfotype(EnumInfoType.LOGIN_RESUALT);
							currUserInfo = new User();
							currUserInfo.setName(info.getFromUser());
							out.writeObject(loginInfo);
							out.flush();

							// 更新服务器用户列表
							listModel.addElement(info.getFromUser());

							StringBuffer sb = new StringBuffer();// 存储用户列表

							// 向其他用户发送更新用户的消息
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

							// 向当前用户发送在线列表
							Info updateInfo = new Info();
							updateInfo.setContent(sb.toString());
							updateInfo.setInfotype(EnumInfoType.LOAD_USER);
							out.writeObject(updateInfo);
							out.flush();
						} else {
							Info loginInfo = new Info();
							loginInfo.setContent("false");
							loginInfo.setFromUser("管理员");
							loginInfo.setSendTime(DateFormatUtil
									.getTime(new Date()));
							loginInfo.setToUser(info.getFromUser());
							loginInfo.setInfotype(EnumInfoType.LOGIN_RESUALT);
							out.writeObject(loginInfo);
							out.flush();
						}
						break;
					// 私聊
					case SEND_INFO:
						for (UserServiceThread user : userThreads) {
							if (user.currUserInfo.getName().equals(
									info.getToUser())) {
								user.out.writeObject(info);
								user.out.flush();
							}
						}
						break;
					// 群聊
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

					// 用户正常退出
					case DELETE_USER:
						exit(info);
						break;
						
					//登录面板退出
					case QUIT:
						flag = false;
						userThreads.remove(this);
						break;
						
					//注册
					case REGISTER:
						if((DataBaseUtil.checkRegisterInfo(info.getFromUser())) && (DataBaseUtil.saveInfo(info))){
							info.setContent("注册成功");
							info.setInfotype(EnumInfoType.REGISTER);
							out.writeObject(info);
							out.flush();
						}else{
							info.setContent("用户名已存在");
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
