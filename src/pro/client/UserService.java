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

	//发送消息方法
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
	
	//注册方法
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
	//登录方法
	public void login(String username,String password) {
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 向服务器发送登录消息
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
	//从服务器接收消息
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
							JOptionPane.showMessageDialog(loginFrame, "用户名或密码错误");
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
						if(info.getContent().equals("用户名已存在")){
							MessageFrame.launchFrame(info.getContent());
						}else if(info.getContent().equals("注册成功")){
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
