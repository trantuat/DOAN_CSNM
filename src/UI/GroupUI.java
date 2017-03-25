package UI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.ComboBoxEditor;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.JEditorPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Client.ClientThread;
import Client.MessageListener;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import Utils.MessageStyle;
import Utils.Constant.Comand;
import Utils.Constant.Response;
public class GroupUI implements ActionListener,MouseListener, MessageListener{

	private JFrame frame;
	private JTextField textFieldMessgae;
	private JTextField textFieldNickName;
	private JButton btnSend;
	private JButton btnBack;
	private JScrollPane scrollPane;
	private int port;
	private Socket socket;
	private boolean isConnected = false;
	private DataOutputStream dos;
	private String username;
	private JList jList;
	private JTextPane textPane;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem addImage;
	private ClientThread thread;
	private JFileChooser fc = new JFileChooser();
	private JMenu mnAccout;
	private JMenuItem mntmLogOut;
	private MainUI main;

	public GroupUI(String username, int port, ClientThread thread, MainUI main) {
		this.thread = thread;
		this.username = username;
		this.port = port;
		this.main = main;
		initialize();
		initData();
	}
	
	private void initData(){
		textFieldNickName.setText(username);
		thread.setListener(this);
		thread.sendMessage(Comand.CMD_REQUEST_ONLINE, username);
		frame.setTitle("You had logged in as: "+username);
	}
	
	public void setVisible(boolean visible){
		frame.setVisible(visible);
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100,527, 359);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textFieldMessgae = new JTextField();
		textFieldMessgae.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(this);
		
		JLabel lblangOnline = new JLabel("Online: ");
		
		JLabel lbl = new JLabel("Username:");
		
		btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnBack.addActionListener(this);
		textFieldNickName = new JTextField();
		textFieldNickName.setEditable(false);
		textFieldNickName.setColumns(10);
		
		textPane = new JTextPane();
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(textPane);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lbl)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldNickName, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBack))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textFieldMessgae, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSend)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblangOnline)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldNickName, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbl)
						.addComponent(lblangOnline))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldMessgae, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE))
					.addGap(16))
		);
		
		jList = new JList();
		jList.addMouseListener(this);
		scrollPane_1.setViewportView(jList);
	
		frame.getContentPane().setLayout(groupLayout);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		addImage = new JMenuItem("Add image");
		addImage.addActionListener(this);
		mnFile.add(addImage);
		
		mnAccout = new JMenu("Account");
		menuBar.add(mnAccout);
		
		mntmLogOut = new JMenuItem("Log out");
		mnAccout.add(mntmLogOut);
		mntmLogOut.addActionListener(this);
	}
	
	public String getUserName(){
		return username;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mntmLogOut){
			int confirm = JOptionPane.showConfirmDialog(null, "Logout your Account.?");
	        if(confirm == 0){
	        	thread.sendMessage(Comand.CMD_LOG_OUT, username);
	        	thread.stop();
	        	this.setVisible(false);
                new LoginUI().setVisible(true);
                frame.dispose();
	        }

		}
		
		if(e.getSource() == btnBack){
			this.main.setVisible(true);
			setVisible(false);
		}
		if (e.getSource() == btnSend){
			String content = username+" "+ textFieldMessgae.getText();
			thread.sendMessage(Comand.CMD_CHAT_ALL, content);
			appendMyMessage(" "+textFieldMessgae.getText(), username);
			textFieldMessgae.setText("");

		}
		
		if(e.getSource() == addImage){
			 fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			 fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
			 fc.setAcceptAllFileFilterUsed(false);
		       
			 int select = fc.showOpenDialog(null);
			 if(select == JFileChooser.APPROVE_OPTION){
				 File file = fc.getSelectedFile();
				 Image image = new ImageIcon(file.getAbsolutePath()).getImage();
				 int height = image.getHeight(null);
				 int width = image.getWidth(null);
				 image = image.getScaledInstance(150, 150 * height / width, Image.SCALE_SMOOTH);
				 textPane.setEditable(true);
				 getMsgHeader(username, Color.ORANGE);
				 int len = textPane.getDocument().getLength();
				 textPane.setCaretPosition(len);
				 textPane.insertIcon(new ImageIcon(image));
				 textPane.replaceSelection("\n\n");
				 textPane.setEditable(false);
				
			 }
		  }
		
	}

	public void appendOnlineList(Vector list){
        jList.removeAll();
        jList.setListData(list);
	}
	/*
	    System Message
	*/
	public void appendMessage(String msg, String header, Color headerColor, Color contentColor){
	    textPane.setEditable(true);
	    getMsgHeader(header, headerColor);
	    getMsgContent(msg, contentColor);
	    textPane.setEditable(false);
	}
	
	/*
	    My Message
	*/
	public void appendMyMessage(String msg, String header){
		textPane.setEditable(true);
	    getMsgHeader(header, Color.ORANGE);
	    getMsgContent(msg, Color.LIGHT_GRAY);
	    textPane.setEditable(false);
	}
	
	/*
	    Message Header
	*/
	public void getMsgHeader(String header, Color color){
	    int len = textPane.getDocument().getLength();
	    textPane.setCaretPosition(len);
	    textPane.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Impact", 13), false);
	    textPane.replaceSelection(header+" : ");
	}
	/*
	    Message Content
	*/
	public void getMsgContent(String msg, Color color){
	    int len = textPane.getDocument().getLength();
	    textPane.setCaretPosition(len);
	    textPane.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Arial", 12), false);
	    textPane.replaceSelection(msg +"\n\n");
	}

	  @Override
	  public void mouseClicked(MouseEvent e) {
//	      ChatUI chat_one = new ChatUI(username, jList.getSelectedValue().toString(),this);
//		  chat_one.setVisible(true);
//	      setVisible(false);
	  }

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String from, String message) {
		appendMessage(message, from, Color.MAGENTA, Color.BLUE);
		
	}
	@Override
	public void update(String from, String to, String message) {
		
	}

	@Override
	public void update(StringTokenizer online) {
		 Vector onlineV = new Vector();
         while(online.hasMoreTokens()){
             String list = online.nextToken();
             if(!list.equalsIgnoreCase(username)){
                 onlineV.add(list);
             }
         }
         appendOnlineList(onlineV);
		
	}

	@Override
	public void error(String message) {
		appendMessage("[Error]: "+ message, "Error", Color.RED, Color.RED);	
	}

}
