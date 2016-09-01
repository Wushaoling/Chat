package pro.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import pro.util.DataBaseUtil;
@SuppressWarnings("all")
public class ServerFrame {

	public JTextArea textArea = new JTextArea();
	private JFrame frame;
	private JTextField textField;
	private ServerService ss;
	private DefaultListModel listModel = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ServerFrame window = new ServerFrame();
					window.frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ServerFrame() {
		initialize();
		startServer();
	}

	// 启动服务器等待连接
	private void startServer() {
		textArea.append("服务器正在运行");
		new Thread(new Runnable() {
			@Override
			public void run() {
				ss = new ServerService(textArea, listModel);
				ss.startServer();
			}
		}).start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u804A\u5929--\u670D\u52A1\u5668");
		frame.setBounds(100, 100, 530, 376);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				DataBaseUtil.closedatabases();
			}
		});
		
		JLabel label = new JLabel("\u7CFB\u7EDF\u65E5\u5FD7\uFF1A ");
		frame.getContentPane().add(label, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel(
				"-----------------\u5728\u7EBF\u7528\u6237\u5217\u8868-----------------");
		panel.add(label_1, BorderLayout.NORTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);

		@SuppressWarnings("rawtypes")
		JList jlist = new JList();
		jlist.setName("list_users");
		listModel = new DefaultListModel();
		listModel.addElement("所有人");
		jlist.setModel(listModel);
		panel.add(jlist, BorderLayout.CENTER);

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
	}

}
