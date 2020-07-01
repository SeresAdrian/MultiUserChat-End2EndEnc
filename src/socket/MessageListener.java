package socket;

import java.io.IOException;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface MessageListener {
	public void onMessage(String fromLogin, String msgBody) throws NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException;
}
