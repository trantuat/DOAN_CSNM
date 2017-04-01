package Client;

public interface AudioCallListener {
	public void beginAudioCall(String from, String to);
	public void endAudioCall(String from, String to);
}