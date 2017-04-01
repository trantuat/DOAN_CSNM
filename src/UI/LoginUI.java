package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;

public class LoginUI implements ActionListener{

	private JFrame frame;
	private JTextField nickname;
	private JButton btnLogin;
	private JTextField port;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 try {
	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
	                if ("Nimbus".equals(info.getName())) {
	                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                    break;
	                }
	            }
	        }catch(Exception e){
	        	
	        }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI window = new LoginUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Log in");
		frame.getContentPane().setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 14));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		JLabel lblNickName = new JLabel("Username : ");
		lblNickName.setHorizontalAlignment(SwingConstants.LEFT);
		
		nickname = new JTextField();
		nickname.setColumns(10);
		
		btnLogin = new JButton("Log in");
		
		btnLogin.addActionListener(this);
		
		JLabel lblPort = new JLabel("Port          : ");
		lblPort.setHorizontalAlignment(SwingConstants.LEFT);
		
		port = new JTextField();
		port.setText("1234");
		port.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(60)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNickName)
								.addComponent(lblPort))
							.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(port)
								.addComponent(nickname, 242, 242, Short.MAX_VALUE))))
					.addGap(60))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(77)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(nickname, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNickName))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(port, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPort))
					.addGap(18)
					.addComponent(btnLogin)
					.addContainerGap(89, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnLogin){

			if(nickname.getText().length() > 0 && port.getText().length() > 0){
				if(nickname.getText().contains(" ")){
					JOptionPane.showMessageDialog(null, "Nick name doesn't contain space!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				MainUI client = new MainUI(nickname.getText(),Integer.parseInt(port.getText()));
				if(client.isConnected()){
					client.setVisible(true);
			     	frame.setVisible(false);
				}else{
					JOptionPane.showMessageDialog(null, "Username is existed!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}else{
				JOptionPane.showMessageDialog(null, "Unvalid username or port, please check it again!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	public void setVisible(boolean visible){
		frame.setVisible(visible);
	}
}
