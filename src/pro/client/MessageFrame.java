package pro.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MessageFrame extends JFrame{
	private static JFrame warnFrame;
	private static JButton button;
	public static void launchFrame(String msg){
		warnFrame = new JFrame();
		warnFrame.setBounds(450, 230, 250, 250);
		warnFrame.setVisible(true);
		warnFrame.setLayout(null);
		
		button = new JButton(msg);
		button.setBounds(70,80,100,50);
		button.addActionListener(new msg());
		warnFrame.add(button);
	}
	public static class msg implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			warnFrame.dispose();
		}
	}
}
