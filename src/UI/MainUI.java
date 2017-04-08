package UI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import Client.Mode;

import javax.swing.JTextField;

import Thread.ClientThread;
import Utils.Constant.Comand;
import Utils.Constant.Host;
import Utils.Constant.Response;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;


public class MainUI implements ActionListener{

	private JFrame frame;
	private JMenuItem mntmLogOut;
	private JLabel lblUsername;
	private JRadioButton rdbtnGroup;
	private JRadioButton rdbtnChat;
	private JButton btnStart;
	
	private Mode mode;
	private String username;
	private int port;
	private Socket socket;
	private DataOutputStream dos;
	private ClientThread thread;
	private boolean isConnected = false;
	



	public MainUI(String nickname, int port) {
		initialize();
		connect(nickname,port);
		
	}
	
	public void connect(String username, int port){
	    this.port = port;
	    this.username = username;
        try {
            socket = new Socket(Host.HOST, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(Comand.CMD_LOG_IN+" "+ username);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            if (success(in)) {
            	thread = new ClientThread(socket,port);
	            new Thread(thread).start();
	            isConnected = true;
	            lblUsername.setText("WELCOME TO "+username.toUpperCase());
	            frame.setTitle("You had logged in as: "+username);
			}else{
				isConnected = false;
			}
            
        }
        catch(IOException e) {
            isConnected = false;
            JOptionPane.showMessageDialog(null, "Unable to Connect to Server, please try again later.!","Connection Failed",JOptionPane.ERROR_MESSAGE);
 
        }
    }
	 
	 private boolean success(DataInputStream dis){
		String data;
		try {
			data = dis.readUTF();
			StringTokenizer st = new StringTokenizer(data);
	         String CMD = st.nextToken();
	         System.out.println(CMD);
	         if(CMD.equals(Response.SUCCESS))
	        	 return true;
		} catch (IOException e) {
			return false;
		}
	    return false;
	 }
	 
	public void setVisible(boolean visible){
		frame.setVisible(visible);
	}
	
	public boolean isConnected(){
		return isConnected;
	}
		

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		ButtonGroup group = new ButtonGroup();
		
		rdbtnGroup = new JRadioButton("Join group chat");
		
		rdbtnChat = new JRadioButton("Chat");
		
		group.add(rdbtnGroup);
		group.add(rdbtnChat);
		rdbtnGroup.setSelected(true);
		mode = Mode.GROUP;
		
		btnStart = new JButton("Let's start");
		
		rdbtnChat.addActionListener(this);
		rdbtnGroup.addActionListener(this);
		btnStart.addActionListener(this);
		
		lblUsername = new JLabel("gdfgdf");
		lblUsername.setForeground(new Color(0, 0, 102));
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(69)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnChat)
						.addComponent(rdbtnGroup)
						.addComponent(btnStart, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addComponent(lblUsername, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
					.addGap(50))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(rdbtnGroup)
					.addGap(18)
					.addComponent(rdbtnChat)
					.addGap(26)
					.addComponent(btnStart)
					.addContainerGap(73, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnAccount = new JMenu("Account");
		menuBar.add(mnAccount);
		
		mntmLogOut = new JMenuItem("Log out");
		mnAccount.add(mntmLogOut);
		mntmLogOut.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == rdbtnChat){
			mode = Mode.CHAT;
		}
		if(e.getSource() == rdbtnGroup){
			mode = Mode.GROUP;
		}
		if(e.getSource() == btnStart){
			startChat();
		}
		
		if(e.getSource() == mntmLogOut){
			logout();
		}
		
	}
	
	private void startChat(){
		switch(mode){
		   case CHAT:
			   ChatUI chat = new ChatUI(username, port, this, thread);
			   chat.setVisible(true);
			   this.setVisible(false);
			   break;
		   case GROUP:
			   GroupUI ui = new GroupUI(username, port, thread,this);
			   ui.setVisible(true);
			   setVisible(false);
			   System.out.print("group");
			   break;
		}
	}
	
	private void logout(){
		int confirm = JOptionPane.showConfirmDialog(null, "Logout your Account.?");
        if(confirm == 0){
        	thread.sendMessage(Comand.CMD_LOG_OUT, username);
        	thread.stop();
        	this.setVisible(false);
            new LoginUI().setVisible(true);
            frame.dispose();
        }
	}
}
