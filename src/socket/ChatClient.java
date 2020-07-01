package socket;

import java.io.BufferedReader;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;


public class ChatClient {
	
	public final String serverName;
	public final int serverPort;
	private Socket socket;
	private OutputStream serverOut;
	private InputStream serverIn;
	private BufferedReader bufferedIn;
	private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
	private ArrayList<MessageListener> messageListeners = new ArrayList<>();
	public ChatClient(String serverName, int serverPort)
	{
		this.serverName=serverName;
		this.serverPort=serverPort;
	}
	public static void main(String[] args) throws IOException {
		ChatClient client  =new ChatClient("localhost",8818);
		if(!client.connect())
		{
			System.err.println("Connection failed");
		}
		else
		{
			System.out.println("Connect successful");
			client.login("guest","guest");
		}
	}
	public boolean login(String login, String password) throws IOException {
		String cmd  = "login " + login + " " + password + "\n";
		serverOut.write(cmd.getBytes());
		
		String response =bufferedIn.readLine();
		System.out.println("Respone Line: "+response);
		
		if ("ok login".equalsIgnoreCase(response))
		{
			startMessageReader();
			return true;
		}
		else
		{
			return false;
		}
	}
	public void startMessageReader() {
		Thread t = new Thread()
				{
			public void run () {
				readMessageLoop();
			}

			
				};
			t.start();
		
	}
	private void readMessageLoop() {
		try {
		String line;
		while ((line = bufferedIn.readLine()) != null)
		{
			String[] tokens = StringUtils.split(line);
			if (tokens != null && tokens.length>0) {
				String cmd = tokens[0];
				if ("online".equalsIgnoreCase(cmd))
				{
					handleOnline(tokens);
				}
				else
					if ("offline".equalsIgnoreCase(cmd))
					{
						handleOffline(tokens);
					}
					else if ("msg".equalsIgnoreCase(cmd))
					{
						String[] tokensMsg = StringUtils.split(line,null, 3);
						handleMessage(tokensMsg);
					}
			}
		}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	private void handleMessage(String[] tokensMsg) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, IOException {
		String login  = tokensMsg[1];
		String msgBody = tokensMsg[2];
		
		for (MessageListener listener: messageListeners)
		{
			listener.onMessage(login, msgBody);
			
		}
		
	}

	private void handleOffline(String[] tokens) {
		String login = tokens[1];
		for (UserStatusListener listener : userStatusListeners)
		{
			listener.offline(login);
		}
	}

	private void handleOnline(String[] tokens) {
		String login = tokens[1];
		for (UserStatusListener listener : userStatusListeners)
		{
			listener.online(login);
		}
		
	}
	public boolean connect() {
		
		try {
			this.socket = new Socket(serverName,serverPort);
			System.out.println("Client port is "+socket.getLocalPort());
			this.serverOut=socket.getOutputStream();
			this.serverIn=socket.getInputStream();
			this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	public void msg(String sendTo, String msgBody) throws IOException {
		// TODO Auto-generated method stub
		String cmd = "msg "+ sendTo + " " + msgBody +"\n";
		serverOut.write(cmd.getBytes());
	}

	public void addUserStatusListener(UserStatusListener listener)
	{
		userStatusListeners.add(listener);
	}
	public void removeUserStatusListener(UserStatusListener listener)
	{
		userStatusListeners.remove(listener);
	}
	public void addMessageListener(MessageListener listener)
	{
		messageListeners.add(listener);
	}
	public void removeMessageListener(MessageListener listener)
	{
		messageListeners.remove(listener);
	}


}
