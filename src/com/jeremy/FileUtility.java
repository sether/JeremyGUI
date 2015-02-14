package com.jeremy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Used to write a content to a file and any other file operations
 * @author AlexBrown
 * @version 1.0
 */
public class FileUtility {

	//TODO: decided to return boolean of successful file write
	//TODO: add ability to append
	
	/**
	 * Writes given content to a specified file<br/>
	 * @param fileName - The name of the file that you wish to write to<br/>
	 * @param fileContent - The content to write to the file<br/>
	 * @return True if file has been successfully written<br/>
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String fileToWrite = "TestData\\TestData.csv";
	 * String fileOutput = "TestData";
	 * 
	 * FileUtility.writeFile(fileToWrite, fileOutput);
	 * </pre>
	 * @throws IOException If file is not writable<br/>
	 */
	public static boolean writeFile(String fileName, String fileContent) throws IOException{
		
		//create file
		File outputFile = new File(fileName);
		outputFile.createNewFile();
		PrintWriter output = new PrintWriter(fileName);
		
		try{			
			
			//write to the file
			output.print(fileContent);
		} finally {
			
			//last thing is to close
			output.close();			
		}
		
		//return true if closed NOTE: remove if not needed
		return true;
	}
	
}
