package Thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import Client.MessageListener;
import Utils.Constant.Comand;

public class ReceiveFileThread implements Runnable {
	private Socket socket;
	private int port;
	private DataInputStream dis;
	private DataOutputStream dos;
	private StringTokenizer st;
    private MessageListener listener;
    private File file;
    private DecimalFormat df = new DecimalFormat("##,#00");
    

    public ReceiveFileThread(Socket socket, int port){
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
    
	@Override
	public void run() {
		 try {
                String data = dis.readUTF();
                String from = "";
                String to = "";
                String length = "";
                String file_name = "";
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
                        length = st.nextToken();
                        try {
                           
                                System.out.println("Receiving File....");
                                String path = "D:\\"+ file_name;
                                FileOutputStream fos = new FileOutputStream(path);
                                InputStream input = socket.getInputStream();
                                long len = Long.parseLong(length);
                                byte[] buffer = new byte[1024];
                                int count, percent = 1;
                                while((count = input.read(buffer)) > 0){
                                    percent = percent + 1;
                                    listener.updateProcess(df.format(percent*1024*100/len) +"% Receiving File...");
                                    fos.write(buffer, 0, count);
                                    System.out.println("Receive "+ count);
                                }
                               
                                fos.flush();
                                fos.close();
                                input.close();
                                listener.completed(file_name,from,to);
                                JOptionPane.showMessageDialog(null, "File was saved at '"+ path +"'");
                                System.out.println("File was saved: "+ path);
                               
                           
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
//                    case Comand.CMD_SEND_ERROR:
//                    	listener.error("Client is not found");
//                    	break;
                    default: 
                        break;
                }
	        } catch(IOException e){
	        }finally{
           	    try {
					socket.close();
					System.out.print("Closed socket receive file");
				} catch (IOException e) {
					System.out.print("Error socket receive file");
					e.printStackTrace();
				}
           }
		
	}

}
