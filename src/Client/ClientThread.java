package Client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ComboBoxEditor;

import UI.GroupUI;
import Utils.SoundEffect;
import Utils.Constant.Comand;


public class ClientThread implements Runnable{
    
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private StringTokenizer st;
    private MessageListener listener;
    
//    public ClientThread(Socket socket, GroupUI main){
//        this.main = main;
//        this.socket = socket;
//        try {
//            dis = new DataInputStream(socket.getInputStream());
//          
//        } catch (IOException e) {
//        	main.appendMessage("[IOException]: "+ e.getMessage(), "Error", Color.RED, Color.RED);
//        }
//    }
    public ClientThread(Socket socket){
        this.socket = socket;
        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
        
        }
    }
    
    public void setListener(MessageListener listener){
    	this.listener = listener;
    }
    
    @Override
    public void run() {
        try {
            while(true){
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                /** Get Message CMD **/
                String CMD = st.nextToken();
                switch(CMD){
                    case Comand.CMD_MESSAGE_GROUP:
                        SoundEffect.MessageReceive.play(); //  Play Audio clip
                        String msg = "";
                        String frm = st.nextToken();
                        while(st.hasMoreTokens()){
                            msg = msg +" "+ st.nextToken();
                        }
                        listener.update(frm, msg);
                        break;
                    case Comand.CMD_MESSAGE_CHAT:
                        SoundEffect.MessageReceive.play(); //  Play Audio clip
                        String msg1 = "";
                        String frm1 = st.nextToken();
                        while(st.hasMoreTokens()){
                            msg1 = msg1 +" "+ st.nextToken();
                        }
                        listener.update(frm1, "", msg1);
                        break;
                        
                    case Comand.CMD_ONLINE:
                    	listener.update(st);
                        break;
                        
                    default: 
                    break;
                }
            }
        } catch(IOException e){
          //  main.appendMessage("[IOException]: "+ e.getMessage(), "Error", Color.RED, Color.RED);
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
