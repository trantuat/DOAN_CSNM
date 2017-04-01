package Thread;

import Client.UpdateTimeListener;

public class TimeThread implements Runnable{
	private boolean isCalling = false;
	private long time;
	private UpdateTimeListener listener;
	
	public TimeThread(UpdateTimeListener listener) {
		isCalling = true;
		time = 0;
		this.listener = listener;
	}
	@Override
	public void run() {
		while(isCalling){
			
			try {
				listener.updateTime(timeCalling(time));
				Thread.sleep(1000);
				time = time + 1000;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void endCalling(){
		isCalling = false;
	}

	private String timeCalling(long time){
		long second = (time / 1000) % 60;
		long minute = (time / (1000 * 60)) % 60;
		long hour = (time / (1000 * 60 * 60)) % 24;

		return String.format("%02d:%02d:%02d", hour, minute, second);
	}
}
