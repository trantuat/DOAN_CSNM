package Client;

import java.util.StringTokenizer;

public interface MessageListener {
	public void update(String from, String message);
	public void update(String from, String to, String message);
	public void update(StringTokenizer online);
	public void error(String message);
}
