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
	
	/**
	 * Writes given content to a specified file<br/>
	 * @param fileName - The name of the file that you wish to write to<br/>
	 * @param fileContent - The content to write to the file<br/>
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
	public static void writeFile(String fileName, String fileContent) throws IOException{
		File outputFile = new File(fileName);
		writeFile(outputFile, fileContent);
	}
	
	/**
	 * Writes given content to a specified file<br/>
	 * @param directory - The name of the directory that your file is located in<br/>
	 * @param fileName - The name of the file that you wish to write to<br/>
	 * @param fileContent - The content to write to the file<br/>
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String directory = " "TestData\\"
	 * String fileToWrite = "TestData.csv";
	 * String fileOutput = "TestData";
	 * 
	 * FileUtility.writeFile(directory, fileToWrite, fileOutput);
	 * </pre>
	 * @throws IOException If file is not writable<br/>
	 */
	public static void writeFile(String directory, String fileName, String fileContent) throws IOException {
		File outputFile = new File(directory, fileName);
		writeFile(outputFile, fileContent);
	}
	
	/**
	 * Writes given content to a specified file<br/>
	 * @param outputFile - The file that you wish to write to<br/>
	 * @param fileContent - The content to write to the file<br/>
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String directory = " "TestData\\"
	 * String fileToWrite = "TestData.csv";
	 * String fileOutput = "TestData";
	 * 
	 * FileUtility.writeFile(new File(directory, fileToWrite), fileOutput);
	 * </pre>
	 * @throws IOException If file is not writable<br/>
	 * @see File
	 */
	public static void writeFile(File outputFile, String fileContent) throws IOException{
		
		//create directory if non-existent
		File parent = outputFile.getParentFile();
		
		if (!parent.exists() && !parent.mkdirs()){
			throw new IllegalStateException("Coulld not create directory: " + parent);
		}
		
		//create file
		outputFile.createNewFile();
		
		//setup output
		PrintWriter output = new PrintWriter(outputFile);
		
		try{			
			
			//write to the file
			output.print(fileContent);
		} finally {
			
			//last thing is to close
			output.close();			
		}
	}
}
