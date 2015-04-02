package com.jeremy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import com.jeremy.SQLHandler.SQLType;

/**
 * Used as a wrapper around the whole API to streamline and ultimately make
 * using the API easier.
 * 
 * @author AlexBrown
 * @version 1.0
 */
public class FileController {

	// TODO: Decide on name of class, JeremyController?
	// TODO: javadoc
	// TODO: decide access modifier for other classes, package maybe?
	// TODO: add ability to change column classes by index
	// TODO: ability to remove column

	public enum OutputType {
		XML, XML_SCHEMA, JSON, SERIALIZED
	}

	private TableData tblData = null;
	private CSVHandler csvHandler;

	private boolean logErrors = true;

	public FileController() {
		csvHandler = new CSVHandler();
	}

	public FileController(boolean logErrors) {
		csvHandler = new CSVHandler();

		this.logErrors = logErrors;
	}

	/**
	 * Reads a CSV file into the tblData TableData object
	 * 
	 * @param fileName
	 *            - The name of the file that you wish to read into the program <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.csv&quot;;
	 * FileController fc = new FileController();
	 * 
	 * fc.readFile(fileName);
	 * </pre>
	 * @throws IOException
	 */
	public void readFile(String fileName) throws IOException {
		try {

			// read in the csv file
			tblData = csvHandler.readCSV(fileName);
		} catch (IOException e) {

			// log the error that occurs
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error reading file: " + fileName, e);
			}
			throw e;
		}
	}

	/**
	 * Reads a CSV file into the tblData TableData object
	 * 
	 * @param directory
	 *            - The name of the path with the file in it
	 * @param fileName
	 *            - The name of the file that you wish to read into the program <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * FileController fc = new FileController();
	 * 
	 * fc.readFile(directory, fileName);
	 * </pre>
	 * @throws IOException
	 */
	public void readFile(String directory, String fileName) throws IOException {
		try {

			// read in the csv file
			tblData = csvHandler.readCSV(directory, fileName);
		} catch (IOException e) {

			// log the error that occurs
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error reading file: " + directory + fileName, e);
			}
			throw e;
		}
	}

	/**
	 * Reads a CSV file into the tblData TableData object
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.readFile(file);
	 * </pre>
	 * @throws Exception
	 */
	public void readFile(File csvFile) throws IOException {
		try {

			// read in the csv file
			tblData = csvHandler.readCSV(csvFile);
		} catch (IOException e) {

			// log the error that occurs
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error reading file: " + csvFile, e);
			}
			throw e;
		}
	}

	/**
	 * Reads a Serialized file into the tblData TableData object
	 * 
	 * @param fileName
	 *            - The name of the file that you wish to read into the program <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.ser&quot;;
	 * FileController fc = new FileController();
	 * 
	 * fc.readSerialized(fileName);
	 * </pre>
	 * @throws Exception
	 */
	public void readSerialized(String fileName) throws Exception {
		try {

			// read in the csv file
			tblData = new Serialized<TableData>().load(new File(fileName));
		} catch (Exception e) {

			// log the error that occurs
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error reading file: " + fileName, e);
			}
			throw e;
		}
	}

	/**
	 * Reads a Serialized file into the tblData TableData object
	 * 
	 * @param directory
	 *            - The name of the path with the file in it
	 * @param fileName
	 *            - The name of the file that you wish to read into the program <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * FileController fc = new FileController();
	 * 
	 * fc.readSerialized(directory, fileName);
	 * </pre>
	 * @throws IOException
	 */
	public void readSerialized(String directory, String fileName)
			throws Exception {
		try {

			// read in the csv file
			tblData = new Serialized<TableData>().load(new File(directory,
					fileName));
		} catch (Exception e) {

			// log the error that occurs
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error reading file: " + directory + fileName, e);
			}
			throw e;
		}
	}

	/**
	 * Reads a Serialized file into the tblData TableData object
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.readSerialized(file);
	 * </pre>
	 * @throws Exception
	 */
	public void readSerialized(File serFile) throws Exception {
		try {
			tblData = new Serialized<TableData>().load(serFile);
		} catch (IOException e) {

			// log the error that occurs
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error reading file: " + serFile, e);
			}
			throw e;
		}
	}

	/**
	 * Outputs data in the specified format to the file specified
	 * 
	 * @param fileName
	 *            - The name of the file you wish to write to
	 * @param outputType
	 *            - The format type that you wish to output as <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String fileName = &quot;TestData.csv&quot;;
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.outputData(fileName, OutputType.XML);
	 * </pre>
	 * @throws IOException
	 */
	public void outputData(String fileName, OutputType outputType)
			throws IOException {

		// creates file object using details
		File file = new File(fileName);

		// sends data onwards
		outputData(file, outputType);
	}

	/**
	 * Outputs data in the specified format to the file specified
	 * 
	 * @param directory
	 *            - The name of the path with the file in it
	 * @param fileName
	 *            - The name of the file you wish to write to
	 * @param outputType
	 *            - The format type that you wish to output as <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String directory = &quot;output/&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.outputData(directory, fileName, OutputType.XML);
	 * </pre>
	 * @throws IOException
	 */
	public void outputData(String directory, String fileName,
			OutputType outputType) throws IOException {

		// creates file object using details
		File file = new File(directory, fileName);

		// sends data onwards
		outputData(file, outputType);
	}

	/**
	 * Outputs data in the specified format to the file specified
	 * 
	 * @param csvFile
	 *            - The file object that you wish to write to
	 * @param outputType
	 *            - The format type that you wish to output as <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String directory = &quot;output/&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.outputData(file, OutputType.XML);
	 * </pre>
	 * @throws IOException
	 */
	public void outputData(File file, OutputType outputType) throws IOException {
		String output = "";

		// get specific output string per output type
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
		case SERIALIZED:

			// write straight to file, no need to return a string
			new Serialized<TableData>().save(tblData, file);
			return;
		}

		try {
			FileUtility.writeFile(file, output);
		} catch (IOException e) {

			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error writing output to file!", e);
			}
			throw e;
		}
	}

	/**
	 * Outputs data in the specified SQL format to a SQL file
	 * 
	 * @param file
	 *            - The file object that you wish to write to
	 * @param databaseName
	 *            - The name of the database
	 * @param sqlType
	 *            - The SQL format type that you wish to output as <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String directory = &quot;output/&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.outputData(file, &quot;Apples&quot;, SQLType.MYSQL);
	 * </pre>
	 * @throws IOException
	 */
	public void outputToSQLFile(File file, String databaseName, SQLType sqlType, boolean identity, int idColumn)
			throws IOException {
		String output = new SQLHandler(tblData).createSQLFile(databaseName,
				sqlType, identity, idColumn);

		try {
			FileUtility.writeFile(file, output);
		} catch (IOException e) {
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error writing output to file!", e);
			}
			throw e;
		}

	}

	/**
	 * Outputs data directly into the database
	 * 
	 * @param host
	 *            - The SQL host that you wish to connect to
	 * @param port
	 *            - The port that the database connects with
	 * @param databaseName
	 *            - The name of the database
	 * @param sqlType
	 *            - The SQL format type that you wish to output as
	 * @param userName
	 *            - The User Name to connect with
	 * @param password
	 *            - The Password to connect with <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 *         localhost:3306/
	 * String host = &quot;localhost&quot;;
	 * String port = &quot;3306&quot;;
	 * String databaseName = &quot;Bananas&quot;;
	 * String userName = &quot;BananaUser&quot;;
	 * String password = &quot;EatAnAppleInstead1&quot;;
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.outputData(host, port, databaseName, SQLType.MYSQL, userName, passWord);
	 * </pre>
	 * @throws SQLException
	 */
	public void outputToDatabase(String host, String port, String databaseName,
			SQLType sqlType, String userName, String password, boolean identity, int idColumn)
			throws SQLException {

		// set up the SQL handler with the table data
		SQLHandler sql = new SQLHandler(tblData);

		try {

			// create the database
			//sql.createDatabase(host + ":" + port, databaseName, sqlType, userName, password);

			// create the table
			//sql.createTable(host + ":" + port, databaseName, sqlType, userName, password, identity, idColumn);

			// insert the data
			sql.insertDatabase(host + ":" + port, databaseName, sqlType,
					userName, password, identity, idColumn);
		} catch (SQLException e) {
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error outputing to database!", e);
			}
			throw e;
		}

	}

	/* Stream-lined methods */

	/**
	 * Reads a csv file into the tblData TableData object and exports it as an
	 * XML File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFile = &quot;TestData.xml&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.csvToXML(file, outputFile);
	 * </pre>
	 * @throws IOException
	 */
	public void csvToXML(File csvFile, File outputFile) throws IOException {
		readFile(csvFile);
		outputData(outputFile, OutputType.XML);
	}
	
	/**
	 * Reads a csv file into the tblData TableData object and exports it as an
	 * XML Schema File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFile = &quot;TestData.xml&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.csvToXML(file, outputFile);
	 * </pre>
	 * @throws IOException
	 */
	public void csvToXMLSCHEMA(File csvFile, File outputFile)
			throws IOException {
		readFile(csvFile);
		outputData(outputFile, OutputType.XML_SCHEMA);
	}

	/**
	 * Reads a csv file into the tblData TableData object and exports it as an
	 * JSON File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFile = &quot;TestData.jsn&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.csvToJSON(file, outputFile);
	 * </pre>
	 * @throws IOException
	 */
	public void csvToJSON(File csvFile, File outputFile) throws IOException {
		readFile(csvFile);
		outputData(outputFile, OutputType.JSON);
	}

	/**
	 * Reads a csv file into the tblData TableData object and exports it as an
	 * SQL File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 * @param databaseName
	 *            - The name of the database that you wish to use <br/>
	 * @param sqlType
	 *            - The type of sql File you wish to write <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String databaseName = &quot;TestData&quot;;
	 * String outputFile = &quot;TestData.sql&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.csvToSQLFile(file, outputFile, databaseName, SQLType.mysql);
	 * </pre>
	 * @throws IOException
	 * @see SQLType
	 */
	public void csvToSQLFile(File csvFile, File outputFile,
			String databaseName, SQLType sqlType, boolean identity, int idColumn) throws IOException {
		readFile(csvFile);
		outputToSQLFile(outputFile, databaseName, sqlType, identity, idColumn);
	}

	/**
	 * Reads a Serialized file into the tblData TableData object and exports it as an XML File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFilePath = &quot;TestData.xml&quot;;
	 * File outputFile = new File(outputFilePath);
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.serializedToXML(file, outputFile);
	 * </pre>
	 * @throws Exception
	 */
	public void serializedToXML(File csvFile, File outputFile) throws Exception {
		readSerialized(csvFile);
		outputData(outputFile, OutputType.XML);
	}

	/**
	 * Reads a Serialized file into the tblData TableData object and exports it as an XML Schema File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFilePath = &quot;TestData.xml&quot;;
	 * File outputFile = new File(outputFilePath);
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.serializedToXMLSCHEMA(file, outputFile);
	 * </pre>
	 * @throws Exception
	 */
	public void serializedToXMLSCHEMA(File csvFile, File outputFile)
			throws Exception {
		readSerialized(csvFile);
		outputData(outputFile, OutputType.XML_SCHEMA);
	}

	/**
	 * Reads a Serialized file into the tblData TableData object and exports it as an JSON File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFilePath = &quot;TestData.jsn&quot;;
	 * File outputFile = new File(outputFilePath);
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.serializedToJSON(file, outputFile);
	 * </pre>
	 * @throws Exception
	 */
	public void serializedToJSON(File csvFile, File outputFile)
			throws Exception {
		readSerialized(csvFile);
		outputData(outputFile, OutputType.JSON);
	}

	/**
	 * Reads a Serialized file into the tblData TableData object and exports it as an SQL File
	 * 
	 * @param csvFile
	 *            - The file object that you wish to read in
	 * @param outputFile
	 *            - The file object that you wish to write to <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String outputFilePath = &quot;TestData.sql&quot;;
	 * File outputFile = new File(outputFilePath);
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.serializedToSQLFile(file, outputFile);
	 * </pre>
	 * @throws Exception
	 */
	public void serializedToSQLFile(File csvFile, File outputFile,
			String databaseName, SQLType sqlType, boolean identity, int idColumn) throws Exception {
		readSerialized(csvFile);
		outputToSQLFile(outputFile, databaseName, sqlType, identity, idColumn);
	}

	/**
	 * Writes an encrypted serialized file with the loaded TableData Object
	 * 
	 * @param file
	 *            - The file object that you wish to read in
	 * @param password
	 *            - the password you wish to use to encrypt the file <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String password = &quot;abcd1234&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.encryptFile(file, password);
	 * </pre>
	 * @throws Exception
	 */
	public void encryptFile(File file, String password) throws Exception {
		try {
			Encrypted<TableData> enc = new Encrypted<TableData>(tblData,
					password);
			 new Serialized<Encrypted<TableData>>().save(enc, file);
		} catch (Exception e) {
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error Encrypting file!", e);
			}
			throw e;
		}
	}

	/**
	 * Reads an encrypted serialized file and loads it into the Table Data Object
	 * 
	 * @param file
	 *            - The file object that you wish to read in
	 * @param password
	 *            - The password you wish to use to decrypt the file <br/>
	 *            <b>USAGE:</b><br/>
	 * 
	 *            <pre>
	 * String password = &quot;abcd1234&quot;;
	 * String fileName = &quot;TestData.csv&quot;;
	 * String directory = &quot;output/&quot;;
	 * File file = new File(directory, fileName);
	 * 
	 * FileController fc = new FileController();
	 * 
	 * fc.decryptFile(file, password);
	 * </pre>
	 * @throws Exception
	 */
	public void decryptFile(File file, String password) throws Exception {
		try {
			 tblData = new Serialized<Encrypted<TableData>>().load(file).decrypt(password);
		} catch (Exception e) {
			if (logErrors) {
				Logging.getInstance().log(Level.SEVERE,
						"Error Encrypting file!", e);
			}
			throw e;
		}
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

	public void setColumnClasses(Class<?> columnClass, int pos) {
		Class<?>[] columnClasses = tblData.getColumnClasses();
		columnClasses[pos] = columnClass;
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
		tblData.setFieldPrecision(fieldPrecision);
		;
	}

	public String getTableName() {
		return tblData.getTableName();
	}

	public void setTableName(String tableName) {
		tblData.setTableName(tableName);
	}

	/* Setters and getters for various settings */

	public boolean isFirstLineUsedAsColumnHeader() {
		return csvHandler.isFirstLineUsedAsColumnHeader();
	}

	public void setFirstLineUsedAsColumnHeader(
		boolean firstLineUsedAsColumnHeader) {
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

	public void removeColumn(int colPos){
		int newFields = tblData.getFields() - 1;
		String[] newColumnHeader = new String[newFields];
		Class<?>[] newColumnClasses = new Class<?>[newFields];
		Object[][] newTableData = new Object[tblData.getLines()][newFields];
		boolean firstPass = true;
		for (int i = 0; i < tblData.getFields(); i++) {
			if (i == colPos && firstPass) {
				firstPass = false;
			} else {
				if (firstPass) {
					newColumnHeader[i] = tblData.getColumnHeader()[i];
					newColumnClasses[i] = tblData.getColumnClasses()[i];
					
					for (int j = 0; j < tblData.getLines(); j++) {
						newTableData[j][i] = tblData.getTableData()[j][i];
					}
				} else {
					newColumnHeader[i - 1] = tblData.getColumnHeader()[i];
					newColumnClasses[i - 1] = tblData.getColumnClasses()[i];
					
					for (int j = 0; j < tblData.getLines(); j++) {
						newTableData[j][i - 1] = tblData.getTableData()[j][i];
					}
				}
				
			}
		}
		tblData.setFields(newFields);
		tblData.setColumnHeader(newColumnHeader);
		tblData.setColumnClasses(newColumnClasses);
		tblData.setTableData(newTableData);
	}

}
