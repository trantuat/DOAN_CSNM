package Thread;

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

import javax.swing.JOptionPane;

import Client.MessageListener;
import Utils.SoundEffect;
import Utils.Constant.Comand;
import Utils.Constant.Response;

public class SendFileThread implements Runnable{
	private Socket socket;
	private int port;
	private DataInputStream dis;
	private DataOutputStream dos;
	private StringTokenizer st;
    private MessageListener listener;
    private File file;
    private DecimalFormat df = new DecimalFormat("##,#00");
   

    public SendFileThread(Socket socket, int port, File file){
        this.socket = socket;
        this.port = port;
        this.file = file;
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
	          //  while(true){
	                String data = dis.readUTF();
	                String file_name = "";
                	String from = "";
                	String to = "";
                	String length = "";
	                st = new StringTokenizer(data);
	                /** Get Message CMD **/
	                String CMD = st.nextToken();
	                switch(CMD){
	                    case Comand.CMD_SEND_FILE:
	                    	/*
	                    	 * Format: CMD_SEND_FILE filename from to
	                    	 */
	                    	file_name = st.nextToken();
	                    	from = st.nextToken();
	                    	to = st.nextToken();
	                    	length  = st.nextToken();
	                    	
	                    	handleSendFile(file_name+" "+from+" "+to+" "+length, from, to);
	                        break;
	                    case Comand.CMD_SEND_ERROR:
	                    	listener.error("Client is not found");
	                    	break;
	                    case Comand.CMD_DENY_RECEIVE_FILE:
	                    	from  = st.nextToken();
	                     	to = st.nextToken();
	                    	JOptionPane.showMessageDialog(null, "User was deny your request !", "Notice", JOptionPane.INFORMATION_MESSAGE);
	                        break;     
	                  
	                    default: 
	                        break;
	                }
	       //     }
	        } catch(IOException e){
	        }
		
	}
	
	 private void handleSendFile(String content, String from, String to) {
	    	/*
	 		 * Format: CMD_SEND_FILE filename sender receiver 
	 		 */
	    	 try {
		            dos.writeUTF(Comand.CMD_SEND_FILE+" "+content);
		            OutputStream output = socket.getOutputStream();
		            /** Read file ***/
		            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
		           
		            /** Create a temporary file storage **/
		            byte[] buffer = new byte[1024];
		            long length = file.length();
		            int count, percent = 0;
		            while((count = bis.read(buffer)) > 0){
		                percent = percent + 1;
		                listener.updateProcess(df.format(percent*100*1024/length) +"% Sending File...");
		                output.write(buffer, 0, count);
		            }
		       
		            output.flush();
		            output.close();
		            bis.close();
		            System.out.println("File was sent..!");
		            listener.completed(file.getName(),from,to);
		             
		     } catch (IOException e1) {
		        	listener.error("Error close socket send file");
		        	listener.error(e1.getMessage());
		     }
	    	 finally{
            	 try {
					socket.close();
					System.out.println("closd socket send file");
				} catch (IOException e) {
					System.out.println("Error close socket send file");
					listener.error("Error close socket send file");
					listener.error(e.getMessage());
				}
            }
		}

}
