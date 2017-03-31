package Thread;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ComboBoxEditor;
import javax.swing.JOptionPane;

import Client.MessageListener;
import UI.GroupUI;
import Utils.Constant.Response;
import Utils.SoundEffect;
import Utils.Constant.Comand;


public class ClientThread implements Runnable{
    
	private Socket socket;
	private int port;
	private DataInputStream dis;
	private DataOutputStream dos;
	private StringTokenizer st;
    private MessageListener listener;
    private DecimalFormat df = new DecimalFormat("##,#00");
    

    public ClientThread(Socket socket, int port){
        this.socket = socket;
        this.port = port;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
        
        }
    }
    
    public void setListener(MessageListener listener){
    	this.listener = listener;
    }
    
    public void setPort(int port){
    	this.port = port;
    }
    
    @Override
    public void run() {
        try {
            while(true){
                String data = dis.readUTF();
                String file_name;
                String msg = "";
                String from = "";
                String to = "";
                String length = "";
                st = new StringTokenizer(data);
                /** Get Message CMD **/
                String CMD = st.nextToken();
                switch(CMD){
                    case Comand.CMD_MESSAGE_GROUP:
                    	/*
                    	 * Format: CMD_MESSAGE_GROUP from message
                    	 */
                        SoundEffect.MessageReceive.play(); //  Play Audio clip
                        
                        from = st.nextToken();
                        
                        while(st.hasMoreTokens()){
                            msg = msg +" "+ st.nextToken();
                        }
                        
                        listener.update(from, msg);
                        break;
                    case Comand.CMD_MESSAGE_CHAT:
                        SoundEffect.MessageReceive.play(); //  Play Audio clip
                        /*
                    	 * Format: CMD_MESSAGE_CHAT message from to
                    	 */
                        msg = "";
                        from = st.nextToken();
                        while(st.hasMoreTokens()){
                            msg = msg +" "+ st.nextToken();
                        }
                        listener.update(from, "", msg);
                        break;
                        
                    case Comand.CMD_ONLINE:
                    	listener.update(st);
                        break;
                    case Comand.CMD_REQUEST_SEND_FILE:
                    	/*
                    	 * Format: CMD_REQUEST_SEND_FILE file_name from to
                    	 */
                    	
                    	 SoundEffect.FileSharing.play();
                    	 // get content 
                    	 file_name = st.nextToken();
                    	 from  = st.nextToken();
                    	 to = st.nextToken();
                    	 length = st.nextToken();
                    	 
                    	 System.out.println("You will receive a " + file_name+" from "+from);
                    	 int confirm = JOptionPane.showConfirmDialog(null, "You will receive file "+file_name+" from "+from+ ". Do you want to accept it ?");
	              		   if(confirm == 0){
	              			   Socket s = new Socket("localhost", port);
	              			   DataOutputStream out = new DataOutputStream(s.getOutputStream());
	              			   out.writeUTF(Comand.CMD_REQUEST_RECEIVE_FILE+" "+file_name+" "+from+" "+to+" "+length);
	              			   
	              			   /*
	              			    * Start thread receive file
	              			    */
	              			   ReceiveFileThread thread = new ReceiveFileThread(s,port);
	              			   thread.setListener(listener);
	              			   new Thread(thread).start();
	              			   SoundEffect.FileSharing.stop();
	              		   }else{
	              			   sendMessage(Response.DENY, file_name+" "+from+" "+to);
	              			   SoundEffect.FileSharing.stop();
	              		   }
                         break;

                    case Response.DENY:
                    	JOptionPane.showMessageDialog(null, "File wouldn't be sent, because receiver didn't accept it !", "Notice", JOptionPane.INFORMATION_MESSAGE);
                        break;               
                    default: 
                    break;
                }
            }
        } catch(IOException e){
               listener.error(e.getMessage());
        }
    }
    


	public void sendMessage(String cmd, String content){
    	 try {
	            dos.writeUTF(cmd+" "+ content);
	        } catch (IOException e1) {
	        	listener.error(e1.getMessage());
	        }
    } 

    public void stop(){
    	try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
