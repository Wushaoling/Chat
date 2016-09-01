package pro.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import pro.model.Info;
import pro.util.DateFormatUtil;
import pro.util.EnumInfoType;

@SuppressWarnings("serial")
public class RegisterFrame extends JFrame {
	// ע�ᴰ�ڿؼ�
	private JButton bt_ROk = new JButton("ע��");
	private JButton bt_RCancle = new JButton("ȡ��");
	private JTextField account = new JTextField();
	private JTextField tf_RName = new JTextField();
	private JPasswordField Rpasswd = new JPasswordField();
	private JLabel lb_RName = new JLabel("������2~10��");
	private JLabel lb_RPasswd = new JLabel("���루6~18��");
	private JLabel lb_RID = new JLabel("�˺ţ�6~10���ֻ���ĸ��");
	private JLabel lb_Gender = new JLabel("�ձ�");
	private JFrame mf_Register;
	private JComboBox<String> cb = new JComboBox<String>();
	private UserService us;

	public RegisterFrame() {
	}

	public RegisterFrame(UserService us) {
		this.us = us;
	}

	// ע���Ӵ���
	public void launchRegisterFrame() {
		mf_Register = new RegisterFrame();
		mf_Register.setBounds(450, 60, 350, 450);
		mf_Register.setVisible(true);
		mf_Register.setLayout(null);
		mf_Register.setTitle("�û�ע��");

		mf_Register.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				mf_Register.dispose();
			}
		});

		bt_ROk.addActionListener(new OK_Lis());
		bt_ROk.setBounds(50, 350, 80, 40);
		bt_RCancle.addActionListener(new Cancel_Lis());
		bt_RCancle.setBounds(200, 350, 80, 40);
		lb_RID.setBounds(20, 20, 150, 20);
		lb_RPasswd.setBounds(20, 100, 100, 20);
		lb_RName.setBounds(20, 180, 100, 20);
		lb_Gender.setBounds(20, 260, 100, 20);
		account.setBounds(70, 50, 200, 30);
		Rpasswd.setBounds(70, 130, 200, 30);
		tf_RName.setBounds(70, 220, 200, 30);

		cb.setBounds(70, 290, 140, 30);
		cb.addItem("male");
		cb.addItem("female");

		mf_Register.add(bt_ROk);
		mf_Register.add(bt_RCancle);
		mf_Register.add(lb_RID);
		mf_Register.add(lb_RPasswd);
		mf_Register.add(lb_RName);
		mf_Register.add(tf_RName);
		mf_Register.add(account);
		mf_Register.add(Rpasswd);
		mf_Register.add(lb_Gender);
		mf_Register.add(cb);
	}

	private class OK_Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (account.getText().length() < 6
					|| account.getText().length() > 12) {
				JOptionPane.showMessageDialog(mf_Register, "�˺ų��Ȳ���ȷ");
				return;
			}
			for (int i = 0; i < account.getText().length(); i++) {
				if (!Character.isLetterOrDigit(account.getText().charAt(i))) {
					JOptionPane.showMessageDialog(mf_Register, "�˺Ÿ�ʽ����ȷ");
					return;
				}
			}
			if (new String(Rpasswd.getPassword()).length() < 6
					|| new String(Rpasswd.getPassword()).length() > 18) {
				JOptionPane.showMessageDialog(mf_Register, "���볤�Ȳ���ȷ");
				return;
			}
			if (tf_RName.getText().length() < 2
					|| tf_RName.getText().length() > 12) {
				JOptionPane.showMessageDialog(mf_Register, "�û������Ȳ���ȷ");
				return;
			}
			Info info = new Info();
			info.setFromUser(account.getText().trim());
			info.setSendTime(DateFormatUtil.getTime(new Date()));
			info.setContent(new String(Rpasswd.getPassword()) + ","
					+ tf_RName.getText() + "," + cb.getSelectedItem());
			info.setToUser("����Ա");
			info.setInfotype(EnumInfoType.REGISTER);
			us.register(info);
		}
	}

	private class Cancel_Lis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mf_Register.dispose();
		}
	}
}
