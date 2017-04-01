package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import Thread.ClientThread;
import Utils.MessageStyle;
import Utils.Constant.Comand;
import Client.MessageListener;

public abstract class BaseUI implements MessageListener, ActionListener,MouseListener{
	
	protected JList jList;
	protected JTextPane textPane;
	protected JMenuItem mntmLogOut;
	protected JButton btnSend;
	protected JTextField textFieldMessage;
	protected JTextField textFieldUserName;
	protected JButton btnBack;
	protected JFrame frame;
	
	protected MainUI main;
	protected ClientThread thread;
	protected String username;
	protected int port;
	
	protected abstract void initialize();
	protected abstract void initData();
	
	public BaseUI(String username, ClientThread thread, MainUI main, int port) {
		this.username = username;
		this.thread = thread;
		this.main = main;
		this.port = port;
		
		initialize();
		initData();
	}
	
	
	/*
	    System Message
	*/
	protected void appendMessage(String msg, String header, Color headerColor, Color contentColor){
	    textPane.setEditable(true);
	    getMsgHeader(header, headerColor);
	    getMsgContent(msg, contentColor);
	    textPane.setEditable(false);
	}
	
	/*
	    My Message
	*/
	protected void appendMyMessage(String msg, String header){
		textPane.setEditable(true);
	    getMsgHeader(header, Color.ORANGE);
	    getMsgContent(msg, Color.LIGHT_GRAY);
	    textPane.setEditable(false);
	}
	
	/*
	    Message Header
	*/
	protected void getMsgHeader(String header, Color color){
	    int len = textPane.getDocument().getLength();
	    textPane.setCaretPosition(len);
	    textPane.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Impact", 13), false);
	    textPane.replaceSelection(header+" : ");
	}
	/*
	    Message Content
	*/
	protected void getMsgContent(String msg, Color color){
	    int len = textPane.getDocument().getLength();
	    textPane.setCaretPosition(len);
	    textPane.setCharacterAttributes(MessageStyle.styleMessageContent(color, "Arial", 12), false);
	    textPane.replaceSelection(msg +"\n\n");
	}
	

	protected void appendOnlineList(Vector list){
        jList.removeAll();
        jList.setListData(list);
	}
	
	public void setVisible(boolean isVisible){
		frame.setVisible(isVisible);
	}

	
	
	@Override
	public void update(String from, String message) {
	

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
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btnBack){
			main.setVisible(true);
			setVisible(false);
		}
		
		if(arg0.getSource() == mntmLogOut){
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

	@Override
	public void completed(String msg, String from, String to) {
		frame.setTitle("You had logged in as: "+username);
		if(!from.equals(username)){
			appendMessage(msg, from, Color.MAGENTA, Color.BLUE);
		}else{
			appendMyMessage(msg, from);
		}
		
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
	
}
