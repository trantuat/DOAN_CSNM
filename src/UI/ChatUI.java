package UI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;

import Thread.ClientThread;
import Thread.SendFileThread;
import Utils.Constant.Comand;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class ChatUI  extends BaseUI implements ActionListener, DocumentListener {

	private JButton btnSendFile;
	private JFileChooser fc = new JFileChooser();
	private String sendTo;
	private File file;
	private boolean isSendFile = false;

	
	
	
	public ChatUI(String username, int port, MainUI main, ClientThread thread) {
		super(username, thread, main, port);
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
		
		textFieldMessage.getDocument().addDocumentListener(this);
		
	    btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(this);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		btnSendFile = new JButton("File");
		btnSendFile.addActionListener(this);
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
							.addComponent(textFieldMessage, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSendFile)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnSend)
							.addGap(22))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
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
						.addComponent(btnSend)
						.addComponent(btnSendFile))
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
				if(!isSendFile){
					String content = username+" "+sendTo+" "+ textFieldMessage.getText();
					thread.sendMessage(Comand.CMD_CHAT_ONE, content);
					appendMyMessage(" "+textFieldMessage.getText(), username);
					
				}else{
					sendFile();
				}
				isSendFile = false;
				textFieldMessage.setText("");
				
			}
			if(arg0.getSource() == btnSendFile){
				
				int select = fc.showOpenDialog(null);
				 if(select == JFileChooser.APPROVE_OPTION){
					 file = fc.getSelectedFile();
					 textFieldMessage.setText(file.getName().toString());
					 isSendFile = true;	
				 }
			}
		}

	private void sendFile() {
		try {
			Socket socket = new Socket("localhost",port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			SendFileThread sendTh = new SendFileThread(socket, port,file);
			sendTh.setListener(this);
			dos.writeUTF(Comand.CMD_REQUEST_SEND_FILE+" "+file.getName()+" "+ username+" "+sendTo +" "+String.valueOf(file.length()));
			
			/*
			 * Start thread send file
			 */
			new Thread(sendTh).start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			error(e.getMessage());
		} catch (IOException e) {
			error(e.getMessage());
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

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		isSendFile = false;
		
	}
	@Override
	public void updateProcess(String percent) {
		frame.setTitle(percent);
		
	}

}
