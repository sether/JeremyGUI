package com.jeremy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * A simple container class for encrypting and decrypting objects. Stores serialized object in an encrypted byte array.
 * @author Scott Micklethwaite
 *
 * @param <T>
 */
public class Encrypted<T extends Serializable> implements Serializable{
	private byte[] encData;
	
	public Encrypted(T object, String password) throws Exception{
		encrypt(object, password);
	}
	
	/**
	 * Converts a string an encryption key.
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private SecretKey passwordToKey(String password) throws Exception{
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		byte[] key = sha.digest(password.getBytes());
		key = Arrays.copyOf(key, 16);
		return new SecretKeySpec(key, "AES");
	}
	
	/**
	 * Serializes the given object into a byte array and encrypts the array.
	 * @param object the object to be encrypted
	 * @param password the encryption password
	 * @throws Exception
	 */
	private void encrypt(T object, String password) throws Exception{
		//serialize object
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bStream);
		oo.writeObject(object);
		oo.close();
		byte[] ser = bStream.toByteArray();
		
		//encrypt byte array
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, passwordToKey(password));
		encData = cipher.doFinal(ser);
	}
	
	/**
	 * Decrypts the stored byte array and deserialises it into an object.
	 * @param password the encryption password
	 * @return the decrypted object
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T decrypt(String password) throws Exception{
		//decrypt byte array
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, passwordToKey(password));
		byte[] decData = cipher.doFinal(encData);
		
		//deserialise object
		ByteArrayInputStream bs = new ByteArrayInputStream(decData);
		ObjectInput oi = new ObjectInputStream(bs);
		oi.close();
		return (T) oi.readObject();
	}
}
