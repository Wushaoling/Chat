package pro.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import pro.model.Info;
import pro.util.DateFormatUtil;
import pro.util.EnumInfoType;
@SuppressWarnings("all")
/**
 * 
 * @author WSL
 *
 */
public class ClientFrame {
	/**
	 * 用户界面
	 */
	private JFrame frame;
	private JTextField textField;
	private DefaultListModel defaultlist;
	private JLabel label = new JLabel("\u5F53\u524D\u7528\u6237\uFF1A ");
	private JList lists = new JList();
	private JTextArea textArea = new JTextArea();
	
	private UserService us;
	private Info info;

	// 构造方法
	public ClientFrame(Info info, UserService us) {
		initialize();
		label.setText("当前用户：" + info.getToUser());
		this.info = info;
		setData(info);
		this.us = us;
	}

	// 将消息添加至面板
	public void setData(Info info) {
		textArea.append("------------------------------------------\n");
		textArea.append(info.getSendTime() + "\n" + info.getFromUser() + "："
				+ info.getContent() + "\n");
	}

	// 添加用户列表
	public void updateUserList(String info, int n) {
		if (1 == n) // 添加
			defaultlist.addElement(info);
		else if(0 == n) //删除
			defaultlist.removeElement(info);
	}

	//初始化

	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setTitle("\u804A\u5929");
		frame.setBounds(350, 100, 530, 376);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(label, BorderLayout.NORTH);

		frame.addWindowListener(new WindowAdapter() {// 关闭窗口，给服务器发送退出消息
			@Override
			public void windowClosing(WindowEvent arg0) { 
				Info closeinfo = new Info();
				closeinfo.setFromUser(info.getToUser());
				closeinfo.setToUser("管理员");
				closeinfo.setInfotype(EnumInfoType.DELETE_USER);
				us.send(closeinfo);
				us.setFlag(false);
				System.exit(0);
			}
		});
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel(
				"------------\u5728\u7EBF\u7528\u6237\u5217\u8868------------");
		panel.add(label_1, BorderLayout.NORTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);

		//添加用户列表
		defaultlist = new DefaultListModel();
		defaultlist.addElement("所有人");
		lists.setModel(defaultlist);
		panel.add(lists, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel labke = new JLabel(" \u8BF7\u8F93\u5165\uFF1A ");
		panel_1.add(labke, BorderLayout.WEST);

		textField = new JTextField();
		panel_1.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);

		JButton btnNewButton = new JButton(" \u53D1\u9001 ");
		panel_1.add(btnNewButton, BorderLayout.EAST);
		btnNewButton.addActionListener(new Send_Lis());
	}

	//发送按钮监听
	private class Send_Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = textField.getText().trim();
			if (msg == null || msg.equals("")) {
				return;
			}
			String toUser;
			if (null == lists.getSelectedValue()) {
				JOptionPane.showMessageDialog(frame, "请选择联系人！");
				return;
			} else {
				toUser = new String(lists.getSelectedValue().toString());
			}
			Info sendinfo = new Info();
			if (toUser.equals("所有人")) {
				sendinfo.setInfotype(EnumInfoType.SEND_ALL);
			} else {
				sendinfo.setInfotype(EnumInfoType.SEND_INFO);
			}
			sendinfo.setContent(msg);
			sendinfo.setFromUser(info.getToUser());
			sendinfo.setToUser(toUser);
			new DateFormatUtil();
			sendinfo.setSendTime(DateFormatUtil.getTime(new Date()));
			us.send(sendinfo);
			textArea.append("------------------------------------------\n");
			textArea.append("       \t\t" + sendinfo.getSendTime() + "\n" + msg + "\n");
			textField.setText("");
			lists.clearSelection();
		}
	}
}
