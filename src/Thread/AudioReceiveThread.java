package Thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import Client.AudioCallListener;
import Client.UpdateTimeListener;
import Utils.Constant.Comand;

public class AudioReceiveThread implements Runnable{
	private Socket socket = null;
	private DataInputStream soundIn = null;
	private SourceDataLine inSpeaker = null;
	private UpdateTimeListener timeListener;
	private TimeThread timeThread;

    public AudioReceiveThread(Socket socket)
    {
    	this.socket = socket;
        try {
			soundIn = new DataInputStream(socket.getInputStream());
			AudioFormat af = new AudioFormat(8000.0f,8,1,true,false);
		    DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
		    inSpeaker = (SourceDataLine) AudioSystem.getLine(info);
		    inSpeaker.open(af);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void setUpdateListener(UpdateTimeListener timeListener){
    	this.timeListener = timeListener;
    }
    

    public void run()
    {
    	try{
	    	 String data = soundIn.readUTF();
	         StringTokenizer st = new StringTokenizer(data);
	         /** Get Message CMD **/
	         String CMD = st.nextToken();
	         switch(CMD){
	             case Comand.CMD_SEND_AUDIO_CALL:
            		timeThread = new TimeThread(timeListener);
	            	new Thread(timeThread).start();
	            	
	             case Comand.CMD_SEND_REPLY_AUDIO_CALL:
	            	 
			        int bytesRead = 0;
			        byte[] inSound = new byte[1024];
			        inSpeaker.start();
			        InputStream input = socket.getInputStream();
			        while((bytesRead = input.read(inSound)) > -1){
			        	if (!inSpeaker.isOpen()) {
							break;
						}
			        	inSpeaker.write(inSound, 0, bytesRead);
                        System.out.println("Receive "+ bytesRead);
                    }
			        input.close();
			        break;
	            
	         }
        }catch(IOException e){
        
        }finally{
       	    try {
       	    	if(inSpeaker.isRunning())
       	    		inSpeaker.stop();
       	    	inSpeaker.close();
				socket.close();
				System.out.print("Closed socket receive audio call");
			} catch (IOException e) {
				System.out.print("Error closed socket receive audio call");
				e.printStackTrace();
			}
       }
    }
    
    public void endCall(){
    	inSpeaker.stop();
    	inSpeaker.close();
    	if(timeThread!=null){
    		timeThread.endCalling();
    	}
  
    }
}
