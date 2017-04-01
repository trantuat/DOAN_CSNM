package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.LayoutStyle.ComponentPlacement;

import Client.AudioCallListener;
import Client.UpdateTimeListener;
import Thread.AudioCallReceiveThread;
import Thread.AudioCallThread;
import Thread.ClientThread;
import Utils.Constant.Comand;
import Utils.Constant.Response;
import Utils.SoundEffect;

import javax.swing.SwingConstants;

public class AudioCallUI implements ActionListener,UpdateTimeListener{

	public JFrame frame;
	private Socket socket;
	private JLabel lblCalling;
	private JButton btnEndCall;
	private String from;
	private String to;
	private DataOutputStream dos;
	private JButton btnAccept;
	private JButton btnDeny;
	private int port;
	private AudioCallThread audioCallThread ;
	private AudioCallReceiveThread audioCallReceiveThread;
	private ClientThread thread;
	private JLabel lblTimeCalling;
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		 try {
//	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//	                if ("Nimbus".equals(info.getName())) {
//	                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//	                    break;
//	                }
//	            }
//	        }catch(Exception e){
//	        	
//	        }
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					AudioCallUI window = new AudioCallUI();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//
//	/**
//	 * Create the application.
//	 */
//	public AudioCallUI() {
//		initialize();
//	}

	public AudioCallUI(Socket socket ,String from, String to, int port ) {
		this.socket = socket;
		this.from = from;
		this.to = to;
		this.port = port;
		initialize();
	}
	
	public AudioCallUI(Socket socket ,String from, String to, int port, AudioCallThread audioCallThread ) {
		this.socket = socket;
		this.from = from;
		this.to = to;
		this.port = port;
		this.audioCallThread = audioCallThread;
		this.audioCallThread.setTimeListener(this);
		initialize();
		
	}
	
	public void setClientThread(ClientThread thread){
		this.thread = thread;
	}
//	 public void setAudioCallListener(AudioCallListener listener){
//	    	this.listener = listener;
//	 }
//	
	public void setUICall(){
		lblCalling.setText("You are calling to "+to);
		btnAccept.setVisible(false);
		btnDeny.setVisible(false);
	}
	
    public void setUIIncoming(){
    	lblCalling.setText(to+ " are calling to you");
    	btnEndCall.setVisible(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		lblCalling = new JLabel("Calling....");
		lblCalling.setHorizontalAlignment(SwingConstants.CENTER);
		lblCalling.setForeground(Color.RED);
		lblCalling.setFont(new Font("Tahoma", Font.PLAIN, 22));
		
		btnEndCall = new JButton("End Call");
		btnEndCall.addActionListener(this);
		
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(this);
		btnDeny = new JButton("Deny");
		btnDeny.addActionListener(this);
		
		lblTimeCalling = new JLabel("");
		lblTimeCalling.setForeground(Color.RED);
		lblTimeCalling.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblTimeCalling.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(81)
					.addComponent(btnAccept)
					.addPreferredGap(ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
					.addComponent(btnDeny)
					.addGap(62))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(36)
					.addComponent(lblCalling, GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
					.addGap(36))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(148, Short.MAX_VALUE)
					.addComponent(btnEndCall, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
					.addGap(121))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(177)
					.addComponent(lblTimeCalling, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(175))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(50)
					.addComponent(lblCalling)
					.addGap(30)
					.addComponent(lblTimeCalling)
					.addGap(51)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAccept)
						.addComponent(btnDeny))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEndCall)
					.addContainerGap(37, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public void setVisible(boolean visible)
	{
		frame.setVisible(visible);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SoundEffect.VoiceCallReceive.stop();
		if(e.getSource() == btnEndCall){
			 int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to end your conversation?");
		     if(confirm == 0){
		    	 thread.sendMessage(Comand.CMD_END_AUDIO_CALL, from+" "+to);
		     }
			
		}
		
		if(e.getSource() == btnDeny){
			thread.sendMessage(Comand.CMD_DENY_AUDIO_CALL, from + " "+to);
			frame.setVisible(false);
			frame.dispose();
		}
		
		if(e.getSource() == btnAccept){
			btnDeny.setVisible(false);
			btnAccept.setVisible(false);
			btnEndCall.setVisible(true);
			begin();
		}
		
		
	}
	 
	public void endCall(){
		SoundEffect.VoiceCallReceive.stop();
		System.out.println("End call");
		if (audioCallThread != null) {
			audioCallThread.endCall();
		}
		if(audioCallReceiveThread != null){
			audioCallReceiveThread.endCall();
		}
		frame.setVisible(false);
		frame.dispose();
	}
	private void begin() {
		try {
			socket = new Socket("localhost", port);
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(Comand.CMD_ACCEPT_AUDIO_CALL +" "+from+" "+to);
			audioCallReceiveThread = new AudioCallReceiveThread(socket);
			audioCallReceiveThread.setUpdateListener(this);
			new Thread(audioCallReceiveThread).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void updateTime(String time) {
		lblTimeCalling.setText(time);
		
	}
}
