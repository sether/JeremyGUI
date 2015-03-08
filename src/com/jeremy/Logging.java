package com.jeremy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Singleton class used for logging errors to a file. Adds additional messages regarding the operating environment
 * to the logs message section.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class Logging {
	private final String LOG_FILE = "logfile.txt";
	private final String LOG_NAME = this.getClass().getName();
	private final boolean APPEND_LOG_FILE = true;
	
	private static Logging instance; //singleton static instance
	
	private Logger logger;
	private FileHandler fileHandler;
	
	/**
	 * Creates a new Logging object if one has not been created and sets it as the static instance field of this class.
	 * @return the static single instance of Logging created by this class
	 */
	public synchronized static Logging getInstance(){
		if(instance == null){
			instance = new Logging();
		}
		return instance;
	}
	
	/**
	 * A private constructor for initializing a Logging object. Instantiates a Logger object and provides it a file handler that
	 * uses the LOG_FILE field as the log file destination. Additionally the FileHandler has it's formatter set to output simple
	 * text as opposed to XML.
	 * @see Logger
	 * @see FileHandler
	 * @see SimpleFormatter
	 */
	private Logging(){
		logger = Logger.getLogger(LOG_NAME);
		try {
			fileHandler = new FileHandler(LOG_FILE, APPEND_LOG_FILE); //log file path, set to append file
			logger.addHandler(fileHandler);
			
			// use SimpleFormatter to set output as text instead of XML
			SimpleFormatter formatter = new SimpleFormatter();
			fileHandler.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A method for writing error details to a log file. Without an Exception.
	 * @param level the severity of the error
	 * @param message an error message relating to the error
	 * @see Level
	 */
	public void log(Level level, String message){
		log(level, message, null);
	}
	
	/**
	 * A method for writing error details to a log file. With an Exception.
	 * @param level the severity of the error
	 * @param message an error message relating to the error
	 * @param e the exception thrown by the error
	 * @see Level
	 */
	public void log(Level level, String message, Exception e){
		//log using java.util.logging
		logger.log(level, messageExtras(message), e);
	}
	
	/**
	 * A method for appending additional details to the end of an error message. Includes username, hostname and operating system.
	 * @param message message to append further details to
	 * @return original message with additional details appended
	 */
	private String messageExtras(String message){
		//append current logged in user
		message += "\nUSER: " + System.getProperty("user.name");
		
		//append hostname
		try {
			message += "\nHOSTNAME: " + InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e1) {
			message += "\nHOSTNAME: UNAVAILABLE";
		}
		
		//append operating system
		message += "\nOS: " + System.getProperty("os.name");
		
		return message;
	}
}
