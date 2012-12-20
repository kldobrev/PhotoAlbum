package options;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 
 * @author KonstantinDobrev
 *
 * This class deals with password safety and hashing
 */
public class PasswordManager {
	
	/**
	 * Creates a salt to be added to the password
	 * 
	 * @param saltLength is the number of randomly generated numbers 
	 * for the salt
	 * @return the newly created salt as a string variable
	 */
	public static String createSalt(int saltLength) {
		byte [] salt = new byte[saltLength];
		SecureRandom rand = new SecureRandom();
		rand.nextBytes(salt);
		String saltStr = "";
		for(int i = 0; i < salt.length; i++) {
			saltStr += salt[i];
		}
		return saltStr;
	}
	
	/**
	 * Generates a hashed password
	 * 
	 * @param password is the purely entered password
	 * @param salt is the generated salt for the hashing
	 * @return the newly generated hashed password
	 * @throws NoSuchAlgorithmException
	 */
	public static String createHashedPasswordSHA256(String password, String salt) throws NoSuchAlgorithmException   {
		String passWithSalt = password + salt;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(passWithSalt.getBytes());
		byte [] hashedPass = md.digest();
		for(int i = 0; i < 4; i++) {
			md.update(hashedPass);
			hashedPass = md.digest();
		}
		return hashedPassString(hashedPass); 
	}
	
	/**
	 * Converts the byte array password to a string
	 * 
	 * @param hashedPass is the generated password in byte array format
	 * @return the hashed password as as string
	 */
	private static String hashedPassString(byte [] hashedPass) {
		StringBuilder hashedPassStr = new StringBuilder();
		for(int i = 0; i< hashedPass.length; i++) {
			String hex = Integer.toHexString(0xff & hashedPass[i]);
			if(hex.length() == 1) {
				hashedPassStr.append('0');
			}
			hashedPassStr.append(hex);
		}
		return hashedPassStr.toString();
	}
	
}

