package com.jeremy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileController {

	// TODO: Decide on name of class
	// TODO: javadoc
	// TODO: decide access modifier for other classes, package maybe? 

	public enum OutputType {
		XML, XML_SCHEMA, JSON, SQL
	}

	private TableData tblData = null;
	private CSVHandler csvHandler;
	
	private boolean logErrors = true;

	public FileController(){
		csvHandler = new CSVHandler();
	}
	
	public FileController(boolean logErrors){
		csvHandler = new CSVHandler();
		
		this.logErrors = logErrors;
	}
	
	public void readFile(String fileName) {
		try {
			
			//read in the csv file
			tblData = csvHandler.readCSV(fileName);
		} catch (IOException e) {
			
			//log the error that occurs 
			if (logErrors){
				Logging.getInstance().log(Level.SEVERE, "Error reading file: " + fileName, e);				
			}
		}
	}

	public void readFile(String directory, String fileName) {
		try {
			
			//read in the csv file
			tblData = csvHandler.readCSV(directory, fileName);
		} catch (IOException e) {
			
			//log the error that occurs 
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error reading file: " + directory + fileName, e);				
			}
		}
	}

	public void readFile(File csvFile) {
		try {
			
			//read in the csv file
			tblData = csvHandler.readCSV(csvFile);
		} catch (IOException e) {
			
			//log the error that occurs 
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error reading file: " + csvFile, e);				
			}
		}
	}

	public void outputData(String fileName, OutputType outputType) {
		
		//creates file object using details
		File file = new File(fileName);
		
		//sends data onwards
		outputData(file, outputType);
	}

	public void outputData(String directory, String fileName, OutputType outputType) {
		
		//creates file object using details
		File file = new File(directory, fileName);
		
		//sends data onwards
		outputData(file, outputType);
	}

	public void outputData(File file, OutputType outputType) {
		String output = "";

		switch (outputType) {
			case XML:
				output = new XMLHandler(tblData, true).getXMLString();
				break;
			case XML_SCHEMA:
				output = new XMLHandler(tblData, true).getSchemaString();
				break;
			case JSON:
				output = new JSONHandler(tblData).makeJSON();
				break;
			case SQL:
				output = new SQLHandler(tblData).createSQLFile("Test");
				break;
		}

		try {
			FileUtility.writeFile(file, output);
		} catch (IOException e) {
			
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error writing output to file!", e);
			}
		}
	}
	
	/* Internal settings for fileController*/
	
	public boolean isLogErrors() {
		return logErrors;
	}

	public void setLogErrors(boolean logErrors) {
		this.logErrors = logErrors;
	}
	
	/*Setters and getters for tblData*/
	
	public Object[][] getTableData() {
		return tblData.getTableData();
	}

	public void setTableData(Object[][] tableData) {
		tblData.setTableData(tableData);
	}

	public String[] getColumnHeader() {
		return tblData.getColumnHeader();
	}
	
	public String getColumnHeader(int pos) {
		return tblData.getColumnHeader()[pos];
	}

	public void setColumnHeader(String[] columnHeader) {
		tblData.setColumnHeader(columnHeader);
	}

	public Class<?>[] getColumnClasses() {
		return tblData.getColumnClasses();
	}
	
	public Class<?> getColumnClasses(int pos) {
		return tblData.getColumnClasses()[pos];
	}

	public void setColumnClasses(Class<?>[] columnClasses) {
		tblData.setColumnClasses(columnClasses);
	}

	public int getLines() {
		return tblData.getLines();
	}

	public void setLines(int lines) {
		tblData.setLines(lines);
	}

	public int getFields() {
		return tblData.getFields();
	}

	public void setFields(int fields) {
		tblData.setFields(fields);
	}

	public int[] getFieldLength() {
		return tblData.getFieldLength();
	}
	
	public int getFieldLength(int pos) {
		return tblData.getFieldLength()[pos];
	}

	public void setFieldLength(int[] fieldLength) {
		tblData.setFieldLength(fieldLength);
	}

	public int[] getFieldPrecision() {
		return tblData.getFieldPrecision();
	}
	
	public int getFieldPrecision(int pos) {
		return tblData.getFieldPrecision()[pos];
	}

	public void setFieldPrecision(int[] fieldPrecision) {
		tblData.setFieldPrecision(fieldPrecision);;
	}
	
	public String getTableName() {
		return tblData.getTableName();
	}

	public void setTableName(String tableName) {
		tblData.setTableName(tableName);
	}
	
	/*Setters and getters for various settings*/
	
	
	public boolean isFirstLineUsedAsColumnHeader() {
		return csvHandler.isFirstLineUsedAsColumnHeader();
	}

	public void setFirstLineUsedAsColumnHeader(boolean firstLineUsedAsColumnHeader) {
		csvHandler.setFirstLineUsedAsColumnHeader(firstLineUsedAsColumnHeader);
	}

	public String getDateFormat() {
		return csvHandler.getDateFormat();
	}

	public void setDateFormat(String dateFormat) {
		csvHandler.setDateFormat(dateFormat);
	}

	public String getColumnDelimiter() {
		return csvHandler.getColumnDelimiter();
	}

	public void setColumnDelimiter(String columnDelimiter) {
		csvHandler.setColumnDelimiter(columnDelimiter);
	}
	

}
