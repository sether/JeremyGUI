package com.jeremy.junit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jeremy.Logging;

/**
 * A JUnit test class for the Logging class.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class LoggingTest {
	private static byte[] backup;
	private static final String LOG_PATH = System.getProperty("user.dir") + "\\logfile.txt";
	
	/**
	 * Backup existing logfile.txt file if exists
	 */
	@BeforeClass
	public static void backupLogs() throws Exception{
		File file = new File(LOG_PATH);
		
		if(file.exists()){
			backup = Files.readAllBytes(Paths.get(file.toString()));
			file.delete();
		}
	}
	
	/**
	 * Restore existing logfile.txt file if exists
	 */
	@AfterClass
	public static void restoreLogs() throws Exception{
		if(backup != null){
			FileOutputStream fos = new FileOutputStream(LOG_PATH);
			fos.write(backup);
			fos.close();
		}
	}
	
	/**
	 * Removes test log after each test
	 */
	@After
	public void cleanLogs(){
		File file = new File(LOG_PATH);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * Test that an exception log is created
	 */
	@Test
	public void logException() {
		Logging.getInstance().log(Level.SEVERE, "JUNIT EXCEPTION TEST PLEASE IGNORE", new IOException());
		
		File file = new File(LOG_PATH);
		assertTrue("A log file was not created", file.exists());
	}
	
	/**
	 * Test that an info log is created
	 */
	@Test
	public void logNotification() {
		Logging.getInstance().log(Level.INFO, "JUNIT INFO TEST PLEASE IGNORE");
		
		File file = new File(LOG_PATH);
		assertTrue("A log file was not created", file.exists());
	}
}
