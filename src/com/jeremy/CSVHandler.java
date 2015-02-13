package com.jeremy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author AlexBrown
 * @version 1.0
 * Used to convert a CSV file into usable data.
 */
public class CSVHandler {
	private int lines = 0;
	private int fields = 0;
	
	/**
	 * Reads a CSV file into a TableData object and returns it
	 * @param fileName - The name of the file that you wish to read into the program
	 * @return Table data from the CSV file specified
	 * @throws IOException
	 * @see TableData
	 */
	public TableData readCSV(String fileName) throws IOException{
		File csvFile = new File(fileName);
		return readCSV(csvFile);
	}
	
	/**
	 * Reads a CSV file into a TableData object and returns it
	 * @param directory - The directory of the file you wish to read
	 * @param fileName - The name of the file that you wish to read into the program
	 * @return Table data from the CSV file specified
	 * @throws IOException
	 * @see TableData
	 */
	public TableData readCSV(String directory, String fileName) throws IOException{
		File csvFile = new File(directory, fileName);
		return readCSV(csvFile);
	}
	
	
	/**
	 * Reads a CSV file into a TableData object and returns it
	 * @param csvFile - The file that you wish to read into the program
	 * @return Table data from the CSV file specified
	 * @throws IOException
	 * @see TableData
	 */
	public TableData readCSV(File csvFile) throws IOException{
	
		//make sure file is a real file and we can read it
		if (csvFile.exists() && csvFile.isFile() && csvFile.canRead()) {
			//count the lines and columns
			countFileLines(csvFile);
			countFileColumns(csvFile);
			
			//create returnable object
			Object[][] output = new Object[lines][fields];
			
			//open a file to read
			BufferedReader reader = new BufferedReader(new FileReader(csvFile));
			try{
				//loop through file adding 1 to lines each line
				int counter = 0;
				String line;
				while((line = reader.readLine()) != null) {
					//split line up for processing
					String[] fields = line.split(",");
					
					//loop through lines
					for (int i = 0; i < fields.length; i++) {
						//neaten up fields and add them to the output array
						output[counter][i] = fields[i].trim();
					}
					counter++;
				}			
			} finally{
				//close after use or on error
				reader.close();
			}
			
			return new TableData(output, lines, fields);
		} else {
			//TODO: Add API error logging file not found exception
			//throw error if the file is not found or can't read it
			throw new FileNotFoundException("Could not find file: " + csvFile.getAbsoluteFile());
		}
	}
	
	private void countFileLines(File csvFile) throws IOException{
		//set up for file reading
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		lines = 0;
		
		try{
			//loop through file adding 1 to lines each line
			while(reader.readLine() != null) {
				lines++;
			}			
		} finally{
			//close after use or on error
			reader.close();
		}
		
	}
	
	private void countFileColumns(File csvFile) throws IOException{
		//TODO: get min columns
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		fields = 0;
		
		try {
			//read line and make sure its valid
			String line = reader.readLine();
			if (line != null) {
				//neaten up
				line = line.trim();
				
				//split for counting
				String[] columnNames = line.split(",");
				if (line != null && line.length() > 0) {
					//get column amount
					fields = columnNames.length;
				} else {
					return;
				}
			}
		}finally{
			reader.close();
		}
		
	}
	
}