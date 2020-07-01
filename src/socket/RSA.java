package socket;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSA {

	private PrivateKey privateKey;
	private PublicKey publicKey;
	public RSA() throws NoSuchAlgorithmException
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		KeyPair pair = keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}
	public void writeToFile(String path, byte[] key)throws IOException
	{
		File f = new File(path);
		f.getParentFile().mkdirs();
		
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
		
		
	}
	public PrivateKey getPrivateKey()
	{
		return privateKey;
	}
	public PublicKey getPublicKey()
	{
		return publicKey;
	}
}
