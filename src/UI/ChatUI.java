package UI;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import Client.ClientThread;
import Utils.Constant.Comand;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ChatUI  extends BaseUI implements ActionListener {

	private String sendTo;
	
	/**
	 * Create the application.
	 */
	public ChatUI(String username, MainUI main, ClientThread thread) {
		super(username, thread, main);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@Override
	protected void initData(){
		textFieldUserName.setText("");
		thread.setListener(this);
		thread.sendMessage(Comand.CMD_REQUEST_ONLINE, username);
		btnSend.setEnabled(false);
		frame.setTitle("You had logged in as: "+username);
	}
	
	@Override
	protected void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(textPane);
		
		JLabel lblSendto = new JLabel("Send To: ");
		
		textFieldUserName = new JTextField();
		textFieldUserName.setEditable(false);
		textFieldUserName.setColumns(10);
		
		JLabel lblMessage = new JLabel("Message ");
		
		textFieldMessage = new JTextField();
		textFieldMessage.setColumns(10);
		
	    btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(this);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(19)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMessage)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textFieldMessage, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSend)
							.addGap(22))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblSendto)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(textFieldUserName, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnBack)))
							.addGap(24)))
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSendto)
						.addComponent(textFieldUserName, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBack))
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldMessage, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMessage)
						.addComponent(btnSend))
					.addGap(8))
		);
		
		jList = new JList();
		jList.addMouseListener(this);
		scrollPane_1.setViewportView(jList);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnAccount = new JMenu("Account");
		menuBar.add(mnAccount);
		
		mntmLogOut = new JMenuItem("Log out");
		mnAccount.add(mntmLogOut);
		mntmLogOut.addActionListener(this);
		
		frame.getContentPane().setLayout(groupLayout);
		
	}
	
	@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			super.actionPerformed(arg0);
			if(arg0.getSource() == btnSend){
				String content = username+" "+sendTo+" "+ textFieldMessage.getText();
				System.out.println(content);
				thread.sendMessage(Comand.CMD_CHAT_ONE, content);
				appendMyMessage(" "+textFieldMessage.getText(), username);
				textFieldMessage.setText("");
			}
		}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(jList.getSelectedValue()!=null){
			sendTo =  jList.getSelectedValue().toString();
			textFieldUserName.setText(sendTo);
			btnSend.setEnabled(true);
			
		}
		 
	}
	@Override
	public void update(String from, String to, String message) {
		sendTo = from;
		appendMessage(message, from, Color.MAGENTA, Color.BLUE);
		textFieldUserName.setText(sendTo);
		btnSend.setEnabled(true);
	}

}
