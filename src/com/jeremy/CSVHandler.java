package com.jeremy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Used to convert a CSV file into usable data.
 * @author AlexBrown
 * @version 1.0
 */
public class CSVHandler {
	private static final String DEFAULT_COLUMN_NAME = "Column";
	private static final String COLUMN_DELIMITER = ",";
	
	private int lines = 0;
	private int fields = 0;
	
	//TODO: decide default
	private boolean firstLineUsedAsColumnHeader = false;
	
	/**
	 * Reads a CSV file into a TableData object and returns it
	 * @param fileName - The name of the file that you wish to read into the program
	 * @return Table data from the CSV file specified<br/>
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String fileName = "TestData.csv";
	 * 
	 * TableData csvFile = csvHandler.readCSV(fileName);
	 * </pre>
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
	 * @return Table data from the CSV file specified<br/>
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String directory = "TestDirectory";
	 * String fileName = "TestData.csv";
	 * 
	 * TableData csvFile = csvHandler.readCSV(directory, fileName);
	 * </pre>
	 * @throws IOException
	 * @see TableData
	 */
	public TableData readCSV(String directory, String fileName) throws IOException{
		File csvFile = new File(directory, fileName);
		return readCSV(csvFile);
	}
	
	
	/**
	 * Reads an already made CSV file into a TableData object and returns it
	 * @param csvFile - The file that you wish to read into the program
	 * @return Table data from the CSV file specified<br/>
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String directory = "TestDirectory";
	 * String fileName = "TestData.csv";
	 * 
	 * File csvFile = new File(directory, fileName);
	 * 
	 * TableData csvFile = csvHandler.readCSV(csvFile);
	 * </pre>
	 * @throws IOException
	 * @see TableData
	 */
	public TableData readCSV(File csvFile) throws IOException{
	
		//make sure file is a real file and we can read it
		if (csvFile.exists() && csvFile.isFile() && csvFile.canRead()) {
			//count the lines and columns
			countFileLines(csvFile);
			countFileColumns(csvFile);
			
			//get rid of a line if the first line is going to be used as a header
			if (firstLineUsedAsColumnHeader) {
				lines--;
			}
			//create returnable object
			Object[][] output = new Object[lines][fields];
			String[] columnHeader = new String[fields];
			
			//open a file to read
			BufferedReader reader = new BufferedReader(new FileReader(csvFile));
			try{
				//loop through file adding 1 to lines each line
				int counter = 0;
				boolean collectedHeader = false;
				String line;
				while((line = reader.readLine()) != null) {
					//split line up for processing
					String[] fields = line.split(COLUMN_DELIMITER);
					
					if (counter == 0 && firstLineUsedAsColumnHeader && !collectedHeader) { //first line and using the headers and hasn't gotten headers already
						for (int i = 0; i < fields.length; i++) {
							
							//add fields to the columnHeader Array
							columnHeader[i] = fields[i].trim();
						}
						collectedHeader = true;
						continue;
					} else if(counter == 0 && !firstLineUsedAsColumnHeader && !collectedHeader) { //first line and not using the headers and hasn't gotten headers already
						for (int i = 0; i < fields.length; i++) {
							//create a column name
							columnHeader[i] = DEFAULT_COLUMN_NAME + i;
						}
						collectedHeader = true;
					}
					
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
			
			return new TableData(output, columnHeader, lines, fields);
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
		//TODO: get max columns
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		fields = 0;
		
		try {
			//read line and make sure its valid
			String line = reader.readLine();
			if (line != null) {
				//neaten up
				line = line.trim();
				
				//split for counting
				String[] columnNames = line.split(COLUMN_DELIMITER);
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

	public boolean isFirstLineUsedAsColumnHeader() {
		return firstLineUsedAsColumnHeader;
	}

	public void setFirstLineUsedAsColumnHeader(boolean firstLineUsedAsColumnHeader) {
		this.firstLineUsedAsColumnHeader = firstLineUsedAsColumnHeader;
	}
	
}