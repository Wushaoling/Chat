package pro.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import pro.model.Info;
import pro.util.Address;
import pro.util.EnumInfoType;
@SuppressWarnings("all")
public class UserService {

	private String reInfo = "UNKNOWN";
	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private JTextArea jtextarea;
	private ClientFrame clientFrame;
	private boolean flag = true;
	private Thread userthread = null;
	private JFrame loginFrame;
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public UserService() {
	}
	
	public UserService(JFrame loginFrame) {
		this.loginFrame = loginFrame;
		try {
			socket = new Socket(Address.ip, Address.port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (Exception e) {
		}
	}

	//������Ϣ����
	public void send(final Info info) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					out.writeObject(info);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	//ע�᷽��
	public void register(Info info) {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					out.writeObject(info);
					out.flush();
					if(null == userthread){
						userthread = new Thread(new UserThread());
						userthread.setDaemon(true);
						userthread.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
	}
	//��¼����
	public void login(String username,String password) {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// ����������͵�¼��Ϣ
					Info info = new Info();
					info.setContent(password);
					info.setFromUser(username);
					info.setInfotype(EnumInfoType.LOGIN);
					out.writeObject(info);
					out.flush();
					if(null == userthread){
						userthread = new Thread(new UserThread());
						userthread.setDaemon(true);
						userthread.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
	}

	public String RegisterInfo(){
		String str = new String();
		while(!reInfo.equals("UNKNOWN")){
			str = reInfo;
			reInfo = "UNKNOWN";
		}
		return str;
	}
	//�ӷ�����������Ϣ
	class UserThread implements Runnable {
		@Override
		public void run() {
			while (flag) {
				try {
					Info info = (Info) in.readObject();
					switch (info.getInfotype()) {
					
					case LOGIN_RESUALT:
						if(!info.getContent().equals("false")){
							clientFrame = new ClientFrame(info, UserService.this);
							loginFrame.dispose();
						}else{
							JOptionPane.showMessageDialog(loginFrame, "�û������������");
						}
						break;
						
					case ADD_USER:
						clientFrame.updateUserList(info.getContent(), 1);
						break;
						
					case LOAD_USER:
						String[] names = info.getContent().split(",");
						for (String name : names) {
							clientFrame.updateUserList(name, 1);
						}
						break;
						
					case SEND_INFO:
						clientFrame.setData(info);
						break;
						
					case DELETE_USER:
						clientFrame.updateUserList(info.getContent(), 0);
						break;
						
					case REGISTER:
						if(info.getContent().equals("�û����Ѵ���")){
							MessageFrame.launchFrame(info.getContent());
						}else if(info.getContent().equals("ע��ɹ�")){
							MessageFrame.launchFrame(info.getContent());
						}
						break;
						
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
