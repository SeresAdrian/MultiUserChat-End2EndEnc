package socket;

import java.awt.BorderLayout;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;






public class MessagePane extends JPanel implements MessageListener {

	private final ChatClient client;
	private final String login;
	private static boolean check=false;
	private static AES cipher;
	private DefaultListModel<String>listModel = new DefaultListModel<>();
	private JList<String> messageList = new JList<>(listModel);
	private JTextField inputField = new JTextField();
	
	public MessagePane(ChatClient client, String login) {
		this.client=client;
		this.login=login;
		
		client.addMessageListener(this);
		
		setLayout(new BorderLayout());
		add(new JScrollPane(messageList),BorderLayout.CENTER);
		add(inputField,BorderLayout.SOUTH);
		
		inputField.addActionListener(new ActionListener() {
			
		
			public void actionPerformed(ActionEvent e) {
				try {
					String text = inputField.getText();
					client.msg(login, text);
					listModel.addElement("You: "+text);
					inputField.setText("");
				} catch (IOException e1) { 
					e1.printStackTrace();
				}
				
			}
		});
	}
	private static byte[] getKey() {
		String key = "";
		for (int i = 0; i < 2; i++)
			key += Long.toHexString(Double.doubleToLongBits(Math.random()));
		return key.getBytes();
	}
	public void onMessage(String fromLogin, String msgBody)throws NoSuchAlgorithmException,IOException,IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
		if (login.equalsIgnoreCase(fromLogin)) {
			try {
			RSAUtil newRSA= new RSAUtil();
			
			int start=1,end=1000;
			int num = (int)(start + (Math.random()*(end-start)));
			String keyz="";
			File tempFile = new File("src/socket/ver.txt");
			boolean exists = tempFile.exists();
			if (exists == true)
			{
				keyz = Files.readAllLines(Paths.get("src/socket/ver.txt")).get(num);
			}
			else
			{
				keyz = getKey().toString();
			}
			
			System.out.println(keyz);
			 String encryptedString = Base64.getEncoder().encodeToString(newRSA.encrypt(keyz, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB"));
		     String decryptedString = RSAUtil.decrypt(encryptedString, "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm");
		     String newString = AES.encrypt(msgBody, encryptedString);
		     String oldString = AES.decrypt(newString, encryptedString);
		     String line  = fromLogin + " :<ciphertext>: " +newString;
		     String line2 = fromLogin + " :<plaintext>: " + oldString;
			 listModel.addElement(line);
			 listModel.addElement(line2);
			   } catch (NoSuchAlgorithmException e) {
		             System.err.println(e.getMessage());
		        }
		}
		
	}


}
//	 System.out.println(encryptedString);
//   System.out.println(decryptedString);
// System.out.println(newString);
// System.out.println(oldString);