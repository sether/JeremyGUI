package com.jeremy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Used to convert a CSV file into usable data.
 * @author AlexBrown
 * @version 1.0
 */
public class CSVHandler {
	private static final String DEFAULT_COLUMN_NAME = "Column";
	private static final String COLUMN_DELIMITER = ",";
	private static final String DATE_FORMAT = "dd/MM/yyyy";	//locale
	
	private int lines = 0;
	private int fields = 0;
	private int[] fieldLength;
	private int[] fieldPrecision;
	
	
	//TODO: decide default
	private boolean firstLineUsedAsColumnHeader = false;
	
	/**
	 * Reads a CSV file into a TableData object and returns it
	 * NOTE: Default table name is the file name without the extension
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
	 * NOTE: Default table name is the file name without the extension
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
	 * NOTE: Default table name is the file name without the extension
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
			//count the lines and columns as well as the field lengths
			
			countFileColumns(csvFile);
			countFileLines(csvFile);
			countFieldLengths(csvFile);
			
			fieldPrecision = new int[fields];
			for(int i = 0; i < fieldPrecision.length; i++) {
				fieldPrecision[i] = 0;
			}
			
			//get rid of a line if the first line is going to be used as a header
			if (firstLineUsedAsColumnHeader) {
				lines--;
			}
			
			//read in file data, find column classes, get headers and return all info in a datatable
			return new TableData(readFileDataIn(csvFile), findColumnClasses(csvFile), getFileName(csvFile.getName()), getHeaders(csvFile), lines, fields, fieldLength, fieldPrecision);
		} else {
			//TODO: Add API error logging file not found exception
			//throw error if the file is not found or can't read it
			throw new FileNotFoundException("Could not find file: " + csvFile.getAbsoluteFile());
		}
	}
	
	
	private String getFileName(String fileName){
		if (fileName == null) {
			return fileName;
		}
		int extentionPosition = fileName.lastIndexOf(".");
		
		if (extentionPosition == -1) {
			return fileName;
		}
		
		 
		
		return fileName.substring(0, extentionPosition);
	}
	
	/*	other data types not checked
	 * byte		0	
	 * short	0
	 * float	0.0f
	 * char		'\u0000'
	*/
	private Class<?>[] findColumnClasses(File csvFile) throws IOException {
		
		//set up column classes to be defaulted to String
		Class<?>[] columnClasses = new Class[fields];
		for (int i = 0; i < columnClasses.length; i++) {
			columnClasses[i] = String.class;
		}
		
		
		//check to see what are booleans.
		//easiest and no chance of being another type
		checkBoolean(csvFile, columnClasses);
		
		//date should be an easy format as well so check that next
		checkDate(csvFile, columnClasses);
		
		//int or long wont be a decimal so check for decimal beforehand
		checkDouble(csvFile, columnClasses);
		
		//check if other values are int or long
		checkNumber(csvFile, columnClasses);
		
		//rest default to string
		
		return columnClasses;
	}
	
	private void checkBoolean(File csvFile, Class<?>[] columnClasses) throws IOException {

		//create reader
		BufferedReader reader = null;
		try{
			
			//iterate through each column
			for (int i = 0; i < columnClasses.length; i++) {
				
				//set up variables to use
				boolean skippedFirstLine = false;
				boolean isBoolean = true;
				
				//Initialize
				reader = new BufferedReader(new FileReader(csvFile));
				String line;
				
				//iterate through each line
				while ((line = reader.readLine()) != null) {
					
					
					if (!skippedFirstLine && firstLineUsedAsColumnHeader) {
						line = reader.readLine();
						skippedFirstLine = true;
					}
					String[] fields = line.split(COLUMN_DELIMITER);
					
					if (!fields[i].equalsIgnoreCase("true") && !fields[i].equalsIgnoreCase("false")) {
						isBoolean = false;
						break;
					}
					
	
				} 
				if (isBoolean) {
					columnClasses[i] = Boolean.class;
				}
				
			}
		} finally{
			//close after use or on error
			reader.close();
		}
		
	}

	//TODO: allow for multiple date formats
	private void checkDate(File csvFile, Class<?>[] columnClasses) throws IOException{
		//create reader
		BufferedReader reader = null;
		try{
			
			//iterate through each column
			for (int i = 0; i < columnClasses.length; i++) {
				
				//set up variables to use
				boolean skippedFirstLine = false;
				boolean isDate = true;
				
				//Initialize
				reader = new BufferedReader(new FileReader(csvFile));
				String line;
				
				//iterate through each line
				while ((line = reader.readLine()) != null) {
					
					
					if (!skippedFirstLine && firstLineUsedAsColumnHeader) {
						line = reader.readLine();
						skippedFirstLine = true;
					}
					String[] fields = line.split(COLUMN_DELIMITER);
					
					//create date format for testing
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
					try {
						
						//see if date
						dateFormat.parse(fields[i]);
					} catch (ParseException e) {
						
						isDate = false;
						break;
					}
					
	
				} 
				if (isDate) {
					columnClasses[i] = Date.class;
				}
				
			}
		} finally{
			//close after use or on error
			reader.close();
		}
		
	}
	
	//NOTE: falls flat if doubles and ints mix, reverts to string
	private void checkDouble(File csvFile, Class<?>[] columnClasses) throws IOException {
		
		//set up precision and reader
		BufferedReader reader = null;
		try{
			
			//iterate through each column
			for (int i = 0; i < columnClasses.length; i++) {
				
				//set up variables to use
				int maxLength = fieldPrecision[i] = 0;
				int maxPercision = 0;
				boolean skippedFirstLine = false;
				boolean isDouble = true;
				
				//set up reader
				reader = new BufferedReader(new FileReader(csvFile));
				String line;				
				
				//read each line
				while ((line = reader.readLine()) != null) {
					
					//if using first line as header
					if (!skippedFirstLine && firstLineUsedAsColumnHeader) {
						
						//read second line
						line = reader.readLine();
						skippedFirstLine = true;
					}
					
					//split lines into columns
					String[] fields = line.split(COLUMN_DELIMITER);
					
					//split by decimal place
					fields = fields[i].split("\\.");
					
					//if there was only one decimal place
					if (fields.length == 2) {
						try {
							
							//test if both values are integers on either side of the decimal place
							Integer.parseInt(fields[0]);
							Integer.parseInt(fields[1]);		
						} catch (NumberFormatException e) {
							
							//if both are integers, its a double
							isDouble = false;
							break;
						}
						
						//Decimal length in SQL is before and after the decimal place.
						//Source: http://www.w3schools.com/sql/sql_datatypes_general.asp
						
						//update max length
						int tempLength =fields[0].length() + fields[1].length();
						if (tempLength > maxLength) {
							maxLength = tempLength;							
						}
						
						//update max precision
						if (fields[1].length() > maxPercision) {
							maxPercision = fields[1].length();							
						}
					} else {
						//is not a double if no decimal or too many
						isDouble = false;
						break;
					}

				} 
				if (isDouble){					
					//set precision and length
					fieldPrecision[i] = maxPercision;
					fieldLength[i] = maxLength;
					columnClasses[i] = Double.class;
				}
				
			}
		} finally{
			//close after use or on error
			reader.close();
		}
	}

	private void checkNumber(File csvFile, Class<?>[] columnClasses) throws IOException {
		
		//create
		BufferedReader reader = null;
		try{
			
			//iterate through each column
			for (int i = 0; i < columnClasses.length; i++) {
				
				//set up variables to use
				boolean skippedFirstLine = false;
				boolean isInt = true;
				boolean isLong = true;
				
				//Initialize
				reader = new BufferedReader(new FileReader(csvFile));
				String line;
				
				//iterate through each line
				while ((line = reader.readLine()) != null) {
					
					
					if (!skippedFirstLine && firstLineUsedAsColumnHeader) {
						line = reader.readLine();
						skippedFirstLine = true;
					}
					
					try {
						//test if both values are integers on either side of the decimal place
						Integer.parseInt(line.split(COLUMN_DELIMITER)[i]);
					} catch (NumberFormatException e) {
						isInt = false;
						try { 
							Long.parseLong(line.split(COLUMN_DELIMITER)[i]);
						} catch (NumberFormatException ex) {
							isLong = false;
							break;	
						}						
					}
	
				} 
				if (isInt) {
					columnClasses[i] = Integer.class;
				} else if(isLong) {
					columnClasses[i] = Long.class;
				}
				
			}
		} finally{
			//close after use or on error
			reader.close();
		}
		
	}

	private Object[][] readFileDataIn(File csvFile) throws IOException{
		Object[][] output = new Object[lines][fields];
		
		//open a file to read
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		try{
			//loop through file adding 1 to lines each line
			int counter = 0;
			boolean gotHeader = false;
			String line;
			while((line = reader.readLine()) != null) {
				//split line up for processing
				String[] fields = line.split(COLUMN_DELIMITER);
				
				//first line and using the headers and hasn't gotten headers already
				if (counter == 0 && !gotHeader && firstLineUsedAsColumnHeader) { 
					gotHeader = true;
					continue;
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
		return output;
	}
	
	private String[] getHeaders(File csvFile) throws IOException{
		String[] columnHeader = new String[fields];
		
		if (firstLineUsedAsColumnHeader) {
			//read first line of csv File
			BufferedReader reader = new BufferedReader(new FileReader(csvFile));
			try{
				//loop through file adding 1 to lines each line
				String line = reader.readLine();
				String[] fields = line.split(COLUMN_DELIMITER);
				
				for (int i = 0; i < fields.length; i++) {
					
					//add fields to the columnHeader Array
					columnHeader[i] = fields[i].trim();
				}
				
			} finally{
				//close after use or on error
				reader.close();
			}

		} else {
			for (int i = 0; i < fields; i++) {
				//create a column name
				columnHeader[i] = DEFAULT_COLUMN_NAME + i;
			}
		}
		
		return columnHeader;
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
	
	private void countFieldLengths(File csvFile) throws IOException{
		//set up for file reading
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		
		try{
			//
			fieldLength = new int[fields];
			String line;
			boolean skippedFirstLine = false;
			while((line = reader.readLine()) != null) {
				if (!skippedFirstLine && firstLineUsedAsColumnHeader) {
					line = reader.readLine();
					skippedFirstLine = true;
				}
				//Split
				String[] fields = line.split(COLUMN_DELIMITER);
				
				//Count
				for (int i = 0; i < fields.length; i++) {
					if (fieldLength[i] < fields[i].length()) {
						fieldLength[i] = fields[i].length();
					}
				}
				
			}			
		} finally{
			//close after use or on error
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