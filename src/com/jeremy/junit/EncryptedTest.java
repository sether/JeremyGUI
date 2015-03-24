package com.jeremy.junit;

import com.jeremy.Encrypted;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Test;

import com.jeremy.Serialized;

/**
 * A JUnit test class for the Encrypted class.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class EncryptedTest {
	@Test
	public void testEncryptCSVCorrect() throws Exception{
		String password = "testPass";
		
		//load csv into string
		Scanner scanner = new Scanner(new File("TestData/LasData.csv"));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		//encrypt csv
		Encrypted<String> enc = new Encrypted<String>(content, password);
		
		assertEquals("Ouput string does not match input", content, enc.decrypt(password));
	}
	
	@Test (expected = Exception.class)
	public void testEncryptCSVFail() throws Exception{
		String password = "testPass";
		
		//load csv into string
		Scanner scanner = new Scanner(new File("TestData/LasData.csv"));
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		//encrypt csv
		Encrypted<String> enc = new Encrypted<String>(content, password);
		String result = enc.decrypt("incorrect password");
	}
}
