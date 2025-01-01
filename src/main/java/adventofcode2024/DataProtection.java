package adventofcode2024;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.GCMParameterSpec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Advent of code doesn't want data checked in. Encrypt it so it's not an issue.
 */
public class DataProtection {

	static byte[] salt = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, };
	static byte[] iv = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, };
	private String password;

	public DataProtection(String password) {
		assert password != null;
		this.password = password;
	}
	
	public DataProtection() {
		this(System.getProperty("aocpassword"));
	}

	String decryptDay(int dayNum) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65535, 32 * 8);
			SecretKeySpec secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(16 * 8, iv));

			byte[] cipherText = Files.readAllBytes(Path.of("data/day" + dayNum + ".enc"));
			byte[] clearText = cipher.doFinal(cipherText);
			// doing this on windows means that the dumb DOS line delimiters are preserved.
			return new String(clearText).replaceAll("\r\n", "\n");
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
				| IOException e) {
			throw new RuntimeException(e);
		}
	}

	boolean encryptInPlace(int dayNum)
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65535, 32 * 8);
		SecretKeySpec secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(16 * 8, iv));
		Path source = Path.of("data/day" + dayNum + ".dat");
		if (!Files.exists(source))
			return false;

		byte[] clearText = Files.readAllBytes(source);
		byte[] cipherText = cipher.doFinal(clearText);
		Files.write(Path.of("data/day" + dayNum + ".enc"), cipherText);
		Files.deleteIfExists(Path.of("data/day" + dayNum + ".dat"));
		return true;
	}

	public static void main(String[] args)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		DataProtection dataProtection = new DataProtection();
		for (int i = 1; i < 25; ++i) {
			if (dataProtection.encryptInPlace(i))
				System.out.println("encrypting day " + i);
		}
	}
}
