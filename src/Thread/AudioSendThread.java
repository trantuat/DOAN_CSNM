package Thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JOptionPane;

import Utils.Constant;
import Utils.Constant.Comand;
import Client.AudioCallListener;
import Client.UpdateTimeListener;

public class AudioSendThread implements Runnable {
	private Socket socket = null;
	private int port;
	private TargetDataLine microphone = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private AudioCallListener listener;
	private UpdateTimeListener timeListener;
	private TimeThread timeThread;
	private AudioReceiveThread receiveThread;
	

    public AudioSendThread(Socket socket)
    {
          this.socket = socket;
          try {
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			AudioFormat af = new AudioFormat(8000.0f,8,1,true,false);
	    	DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
	    	microphone = (TargetDataLine)AudioSystem.getLine(info);
	    	microphone.open(af);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    }

    public void setAudioCallListener(AudioCallListener listener){
    	this.listener = listener;
    }
    
    public void setTimeListener(UpdateTimeListener timeListener){
    	this.timeListener = timeListener;
    }
    
    public void setPort(int port){
    	this.port = port;
    }
    
    public void run()
    {
    	//while(true){
    		OutputStream output = null;
	    	try {
		    	String data = dis.readUTF();
		     	String from = "";
		     	String to = "";
		        StringTokenizer st = new StringTokenizer(data);
		         /** Get Message CMD **/
		         String CMD = st.nextToken();
		         
		         switch(CMD){
		             case Comand.CMD_SEND_AUDIO_CALL:
		             
		            	from = st.nextToken();
			            to = st.nextToken();
			            
		            	timeThread = new TimeThread(timeListener);
		            	new Thread(timeThread).start();
		            	
		            	microphone.start();	
		            	dos.writeUTF(Comand.CMD_SEND_AUDIO_CALL+" "+from+" "+to);
			    	    int bytesRead = 0;
			    	    byte[] soundData = new byte[1024];
			    	    output = socket.getOutputStream();
			    	    System.out.println("Get audio.");
			    	    while(bytesRead != -1){
			    	        bytesRead = microphone.read(soundData, 0, soundData.length);
			    	        if(bytesRead == 0){
			    	        	break;
			    	        }
			    	    	System.out.println("Send "+bytesRead);
		    	        	output.write(soundData, 0, bytesRead);
			    	    }
			    	    microphone.close();
			    	    dos.close();
			    	    output.close();
			    	    
			    	    break;
		             case Comand.CMD_SEND_REPLY_AUDIO_CALL:
		            	    from = st.nextToken();
				            to = st.nextToken();
			            	
				            if(!microphone.isRunning())
			            		microphone.start();
			            	dos.writeUTF(Comand.CMD_SEND_REPLY_AUDIO_CALL+" "+from+" "+to);
				    	    int bytesRead1 = 0;
				    	    byte[] soundData1 = new byte[1024];
				    	    output = socket.getOutputStream();
				    	    System.out.println("Get audio.");
				    	    while(bytesRead1 != -1){
				    	        bytesRead = microphone.read(soundData1, 0, soundData1.length);
				    	        if(bytesRead == 0){
				    	        	break;
				    	        }
				    	    	System.out.println("Send "+bytesRead);
			    	        	output.write(soundData1, 0, bytesRead);
				    	    }
				    	    microphone.close();
				    	    dos.close();
				    	    output.close();
				    	    break;
		             case Comand.CMD_DENY_AUDIO_CALL:
                    	from  = st.nextToken();
                     	to = st.nextToken();
                    	if(listener!=null){
                    		listener.endAudioCall(from, to);
                    		if(microphone.isRunning())
                    			microphone.stop();
            	    		microphone.close();
                    	}
                    	JOptionPane.showMessageDialog(null, "User was deny your request !", "Notice", JOptionPane.INFORMATION_MESSAGE);
                        break;
		           
	                        
		         }
	    	 } catch (IOException e) {
	    		 try {
	    			 if(output != null)
	    				 output.close();
				} catch (IOException e1) {}
	    		 listener.endAudioCall("", "");
	    		 if(microphone.isRunning())
         			microphone.stop();
	    		 microphone.close();
	    	//	 break;
			 }finally{
	       	    try {
	       	    	if (!socket.isClosed()) {
	       	    		socket.close();
					}
					
					System.out.print("Closed socket send audio call");
				} catch (IOException e) {
					System.out.print("Error closed socket audio call");
					e.printStackTrace();
				}
	       }
	    	
    	}
  //  }
    
    public void endCall(){
    	microphone.stop();
    	microphone.close();
    	if(timeThread!=null){
    		timeThread.endCalling();
    		
    	}
    }
    	
 }
