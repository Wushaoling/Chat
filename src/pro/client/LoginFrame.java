package pro.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import pro.model.Info;
import pro.util.Address;
import pro.util.EnumInfoType;

public class LoginFrame {

	private JFrame frame;

	private UserService us = null;

	private JTextField textfield = new JTextField();
	private JPasswordField pwdfield = new JPasswordField();

	private JButton bu_login = new JButton("��¼");
	private JButton bu_exit = new JButton("�˳�");
	private JButton bu_register = new JButton("ע��");

	private JLabel label2 = new JLabel("����(6~18)");
	private JLabel label1 = new JLabel("�˺�(6~12)");

	private JTextField IPtext = new JTextField();
	private JTextField PORTtext = new JTextField();
	
	private JFrame setFrame = new JFrame();
	
	// ������
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LoginFrame window = new LoginFrame();
					window.frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ���ش���
	public LoginFrame() {
		initialize();
	}

	// ��ʼ������
	private void initialize() {
		// ���ô���
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(450, 150, 400, 300);
		frame.setLayout(null);
		frame.setTitle("�û���¼");
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (us != null) {
					Info info = new Info();
					info.setInfotype(EnumInfoType.QUIT);
					us = new UserService(frame);
					us.send(info);
				}
				System.exit(0);
			}
		});

		bu_login.setBounds(40, 200, 80, 30);
		bu_login.addActionListener(new Login_Lis());

		bu_exit.setBounds(140, 200, 80, 30);
		bu_exit.addActionListener(new Reset_Lis());

		bu_register.setBounds(240, 200, 80, 30);
		bu_register.addActionListener(new Register_Lis());

		label1.setBounds(40, 20, 90, 50);
		label2.setBounds(40, 100, 90, 50);
		textfield.setBounds(120, 30, 150, 30);
		pwdfield.setBounds(120, 110, 150, 30);

		frame.add(bu_login);
		frame.add(bu_exit);
		frame.add(label1);
		frame.add(label2);
		frame.add(textfield);
		frame.add(pwdfield);
		frame.add(bu_register);
		
		JMenuBar mb = new JMenuBar();
		JMenu menu1 = new JMenu("����");
		
		JMenuItem menuitem1 = new JMenuItem("ip���˿�");
		menuitem1.addActionListener(new setAddress());
		
		frame.setJMenuBar(mb);
		
		menu1.add(menuitem1);
		
		mb.add(menu1);
		frame.setVisible(true);
	}

	// �˳���ť����
	private class Reset_Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (0 == JOptionPane.showConfirmDialog(frame, "���Ҫ�˳���", "�˳�",
					JOptionPane.OK_CANCEL_OPTION)) {
				if (us != null) {
					Info info = new Info();
					info.setInfotype(EnumInfoType.QUIT);
					us = new UserService(frame);
					us.send(info);
				}
				System.exit(0);
			}
		}
	}

	// ��¼��ť����
	private class Login_Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (us == null)
				us = new UserService(frame);
			if (!(textfield.getText() == null || textfield.getText().equals(""))) {
				us.login(textfield.getText(),
						new String(pwdfield.getPassword()));
			}
		}
	}
	
	//����ip���˿�
	private class setAddress implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setFrame.setBounds(500, 150, 300, 250);
			setFrame.setLayout(null);
			
			JLabel l1 = new JLabel("IP");
			l1.setBounds(20, 20, 30, 30);
			JLabel l2 = new JLabel("PORT");
			l2.setBounds(20, 80, 30, 30);
			setFrame.add(l1);
			setFrame.add(l2);
			
			IPtext.setBounds(70, 20, 150, 30);
			PORTtext.setBounds(70, 80, 150, 30);
			setFrame.add(IPtext);
			setFrame.add(PORTtext);
			
			JButton set = new JButton("ȷ��");
			set.setBounds(100, 150, 80, 30);
			set.addActionListener(new Save());
			setFrame.add(set);
			
			setFrame.setVisible(true);
		}
	}

	// ע�ᰴť����
	private class Register_Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (us == null) {
				us = new UserService(frame);
			}
			new RegisterFrame(us).launchRegisterFrame();
		}
	}
	
	//����ip��port
	private class Save implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Address.ip = IPtext.getText();
			Address.port = new Integer(PORTtext.getText());
			JOptionPane.showMessageDialog(setFrame, "���óɹ�");
			setFrame.dispose();
			System.out.println(Address.ip + Address.port);
		}
	}
}
