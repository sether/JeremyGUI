package com.jeremy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import com.jeremy.SQLHandler.SQLType;

public class FileController {

	// TODO: Decide on name of class
	// TODO: javadoc
	// TODO: decide access modifier for other classes, package maybe? 

	public enum OutputType {
		XML, XML_SCHEMA, JSON
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
	
	public void readFile(String fileName) throws IOException {
		try {
			
			//read in the csv file
			tblData = csvHandler.readCSV(fileName);
		} catch (IOException e) {
			
			//log the error that occurs 
			if (logErrors){
				Logging.getInstance().log(Level.SEVERE, "Error reading file: " + fileName, e);				
			}
			throw e;
		}
	}

	public void readFile(String directory, String fileName) throws IOException {
		try {
			
			//read in the csv file
			tblData = csvHandler.readCSV(directory, fileName);
		} catch (IOException e) {
			
			//log the error that occurs 
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error reading file: " + directory + fileName, e);				
			}
			throw e;
		}
	}

	public void readFile(File csvFile) throws IOException {
		try {
			
			//read in the csv file
			tblData = csvHandler.readCSV(csvFile);
		} catch (IOException e) {
			
			//log the error that occurs 
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error reading file: " + csvFile, e);				
			}
			throw e;
		}
	}

	public void outputData(String fileName, OutputType outputType) throws IOException {
		
		//creates file object using details
		File file = new File(fileName);
		
		//sends data onwards
		outputData(file, outputType);
	}

	public void outputData(String directory, String fileName, OutputType outputType) throws IOException {
		
		//creates file object using details
		File file = new File(directory, fileName);
		
		//sends data onwards
		outputData(file, outputType);
	}

	public void outputData(File file, OutputType outputType) throws IOException {
		String output = "";

		switch (outputType) {
			case XML:
				output = new XMLHandler(tblData, true).getXMLString();
				break;
			case XML_SCHEMA:
				output = new XMLHandler(tblData, true).getSchemaString();
				break;
			case JSON:
				output = new JSONHandler(tblData).stringifyJSON();
				break;
		}

		try {
			FileUtility.writeFile(file, output);
		} catch (IOException e) {
			
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error writing output to file!", e);
			}
			throw e;
		}
	}
	
	public void outputToSQLFile(File file, String databaseName, SQLType sqlType) throws IOException {
		String output = new SQLHandler(tblData).createSQLFile(databaseName, sqlType);
		
		try {
			FileUtility.writeFile(file, output);
		} catch (IOException e) {
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE, "Error writing output to file!", e);
			}
			throw e;
		}
		
	}
	
	public void outputToDatabase(String host, String port, String databaseName, SQLType sqlType, String userName, String password) {
		SQLHandler sql = new SQLHandler(tblData);
		
		sql.createDatabase(host + ":" + port, databaseName, sqlType, userName, password);
		sql.createTable(host + ":" + port, databaseName, sqlType, userName, password);
		sql.insertDatabase(host + ":" + port, databaseName, sqlType, userName, password, false);
	}
	
	
	/* Stream-lined methods */
	
	public void csvToXML(File csvFile, File outputFile) throws IOException{
		readFile(csvFile);
		outputData(outputFile, OutputType.XML);
	}
	
	public void csvToXMLSCHEMA(File csvFile, File outputFile) throws IOException{
		readFile(csvFile);
		outputData(outputFile, OutputType.XML_SCHEMA);
	}
	
	public void csvToJSON(File csvFile, File outputFile) throws IOException {
		readFile(csvFile);
		outputData(outputFile, OutputType.XML);
	}
	
	public void csvToSQLFile(File csvFile, File outputFile, String databaseName, SQLType sqlType) throws IOException{
		readFile(csvFile);
		outputToSQLFile(outputFile, databaseName, sqlType);
	}
	
	/* Internal settings for fileController */
	
	public boolean isLogErrors() {
		return logErrors;
	}

	public void setLogErrors(boolean logErrors) {
		this.logErrors = logErrors;
	}
	
	/* Setters and getters for tblData */
	
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
