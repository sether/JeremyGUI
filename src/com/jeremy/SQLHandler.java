package com.jeremy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.sql.DatabaseMetaData;

/**
 * Class to convert a TableData object into a SQL file
 * 
 * @author Ryan Kavanagh
 * @version 1.0
 */
public class SQLHandler {
	private TableData tblData;
	private Connection connection = null;
	
	//Enum for choosing the SQL database
	public enum SQLType {
		SQLSERVER, MYSQL, POSTGRESQL
	};

	private static String connectionURL = "";
 
	/**
	 * Gets the TableData, also the class's constructor method
	 * 
	 * @param data - The data from the specified .csv file
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * CSVHandler csvHandler = new CSVHandler();
	 * csvHandler.setFirstLineUsedAsColumnHeader(true);
	 * 
	 * TableData tableData = csvHandler.readCSV("TestData/testDataType.csv");
	 * 
	 * SQLHandler sqlHandler = new SQLHandler(tableData);
	 *
	 * </pre>
	 * @see TableData
	 * @see CSVHandler
	 */
	public SQLHandler(TableData data) {
		tblData = data;
	}

	private String getHost(SQLType sqlType){
		String host = "";
		if (sqlType == SQLType.SQLSERVER) {
			host = "localhost:1433;";
		} else if (sqlType == SQLType.MYSQL) {
			host = "localhost:3306/";
		} else if (sqlType == SQLType.POSTGRESQL) {
			host = "localhost:5432/";
		}
		return host;
	}
	
	private String getConnectionURL(SQLType sqlType, String host, String databaseName){
		String connectionURL = "";
		if (sqlType == SQLType.SQLSERVER) {
			connectionURL = "jdbc:sqlserver://" + host + "/" + databaseName + ";";
		} else if (sqlType == SQLType.MYSQL) {
			connectionURL = "jdbc:mysql://" + host  + "/" + databaseName;
		} else if (sqlType == SQLType.POSTGRESQL) {
			connectionURL = "jdbc:postgresql://" + host  + "/" + databaseName;
		}
		return connectionURL;
	}

	private String getIDField(SQLType sqlType){
		String idField = "";
		if (sqlType == SQLType.SQLSERVER) {
			idField = "ID int IDENTITY(1,1),";
		} else if (sqlType == SQLType.MYSQL) {
			idField = "id INT NOT NULL AUTO_INCREMENT,";
		} else if (sqlType == SQLType.POSTGRESQL) {
			idField = "id SERIAL,";
		}
		return idField;
	}
	
	private String getFields(){
		Class<?>[] columnClasses = tblData.getColumnClasses();
		Object[] headings = tblData.getColumnHeader();
		String fields = "";
		String dataType = "";
		int cols = tblData.getFields();
		//Gets the unique identification field for the desired SQL database
		for (int i = 0; i < cols; i++) {
			if (columnClasses[i] == Integer.class) {
				dataType = "INT, ";
			} else if (columnClasses[i] == Double.class) {
				dataType = "DECIMAL(" + tblData.getFieldLength()[i] + ","
						+ tblData.getFieldPrecision()[i] + "), ";
			} else if (columnClasses[i] == Date.class) {
				dataType = "DATE, ";
			} else if (columnClasses[i] == Long.class) {
				dataType = "BIGINT, ";
			} else {
				dataType = "VARCHAR(" + tblData.getFieldLength()[i] + "), ";
			}
			fields += headings[i] + " " + dataType + "\n";
		}
		return fields;
	}
	
	/**
	 * Directly creates the Database for the designated SQL database type
	 * 
	 * @param host - A string where the user can designate the databases file path, defaults to local host 
	 * @param databaseName - A string that specifies the name of the database being created
	 * @param sqlType - An enum that specifies which designates what SQL type the database shall be
	 * @param userName - A string that specifies the user name of the creator for SQL access
	 * @param password - A string that specifies the password of the creator for SQL access
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String databaseName = "Example";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * CSVHandler csvHandler = new CSVHandler();
	 * csvHandler.setFirstLineUsedAsColumnHeader(true);
	 * 
	 * TableData tableData = csvHandler.readCSV("TestData/testDataType.csv");
	 * 
	 * SQLHandler sqlHandler = new SQLHandler(tableData);
	 * sqlHandler.createDatabase(host, databaseName, MYSQL, userName, password);
	 * 
	 * </pre>
	 * @throws SQLException
	 * @see TableData
	 * @see CSVHandler
	 */
	private void createDatabase(String host, String databaseName,
			SQLType sqlType, String userName, String password) throws SQLException{
		Connection connection = null;
		Statement statement = null;
		try {
			//If no host has been declared default to local host
			if (host.equalsIgnoreCase("")) {
				host = getHost(sqlType);
			}
			//Creating the connection String used to connect to the database
			connectionURL = getConnectionURL(sqlType, host, "");
			connection = DriverManager.getConnection(connectionURL, userName, password);
			//Statement used to write and execute SQL commands
			statement = connection.createStatement();
			String createDatabase = "CREATE DATABASE " + databaseName;
			statement.executeUpdate(createDatabase);
		} catch (SQLException se) {
			throw(se);
		} finally {
			try {
				//Closes the statement if it was opened
				if (statement != null)
					statement.close();
			} catch (SQLException se) {
				throw(se);
			}
			try {
				//Closes the connection if it was opened
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				throw(se);
			}
		}
	}

	/**
	 * Directly creates a Table for the designated SQL database type
	 * 
	 * @param host - A string where the user can designate the databases file path, defaults to local host 
	 * @param databaseName - A string that specifies the name of the database that the table will be created in
	 * @param sqlType - An enum that specifies which designates what SQL type the database shall be
	 * @param userName - A string that specifies the user name of the creator for SQL access
	 * @param password - A string that specifies the password of the creator for SQL access
	 * @param identity - A boolean that specifies whether to create a default id field
	 * @param idColumn - An integer that specifies which column to be the id column(Cannot be used if identity is true)
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String databaseName = "Example";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * CSVHandler csvHandler = new CSVHandler();
	 * csvHandler.setFirstLineUsedAsColumnHeader(true);
	 * 
	 * TableData tableData = csvHandler.readCSV("TestData/testDataType.csv");
	 * 
	 * SQLHandler sqlHandler = new SQLHandler(tableData);
	 * sqlHandler.createTable(host, databaseName, MYSQL, userName, password, true, -1);
	 * 
	 * </pre>
	 * @throws SQLException
	 * @throws Error
	 * @see TableData
	 * @see CSVHandler
	 */
	private void createTable(String host, String databaseName, SQLType sqlType,
			String userName, String password, boolean identity, int idColumn)  throws SQLException, Error{
		Statement statement = null;
		try {
			//If no host has been declared default to local host
			if (host.equalsIgnoreCase("")) {
				host = getHost(sqlType);
				if(sqlType == SQLType.SQLSERVER){
					host += "databaseName=" + databaseName + ";";
				}
			}
			//Creating the connection String used to connect to the database
			//Gets the unique identification field for the desired SQL database
			connectionURL = getConnectionURL(sqlType, host, databaseName);
			try{
				connection = DriverManager.getConnection(connectionURL, userName, password);
			}catch(SQLException se){
				
			}finally{
				if(connection == null){
					createDatabase(host, databaseName, sqlType, userName, password);
				}	
			}
			connectionURL = getConnectionURL(sqlType, host, databaseName);
			connection = DriverManager.getConnection(connectionURL, userName, password);
			//Statement used to write and execute SQL commands
			statement = connection.createStatement();
			String tableName = tblData.getTableName();
			String fields = getFields();
			String idField = "";
			String primaryKey = "";
			int cols = tblData.getFields();
			Object[] headings = tblData.getColumnHeader();
			if(identity & idColumn != -1){
				throw new Error("Conflict with 'identity' and 'idColumn' paramaters. Both cannot be valid, change 'identity' to false or 'idColumn' to -1");
			}else if(identity){
				idField = getIDField(sqlType) + "\n";
				primaryKey = "id";
			}else if(idColumn > cols || idColumn < 0){
				throw new Error("Conflict with 'idColumn' paramater. 'idColumn' cannot be less than 0 or greater than the columns in the table");
			}else{
				primaryKey += headings[idColumn];
			}
			String createTable = "CREATE TABLE " + tableName + "(\n"
					+ idField + fields + "PRIMARY KEY (" + primaryKey + "));";
			statement.executeUpdate(createTable);
		} catch (SQLException se) {
			throw(se);
		} finally {
			try {
				//Closes the statement if it was opened
				if (statement != null)
					statement.close();
			} catch (SQLException se) {
				throw(se);
			}
			try {
				//Closes the connection if it was opened
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				throw(se);
			}
		}
	}

	/** 
	 * Creates the insert statement for the designated SQL database type, used inside of another method(e.g. insertDatabase(), createSQLFile())
	 * 
	 * @param tableName - A string that specifies the name of the database that the table will be created in
	 * @param tableFields - A String specifying the fields that are in the table	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * final int batchSize = 1000;
	 * int count = 0;
	 * Object[][] data = tblData.getTableData();
	 * int line = 1;
	 * int rows = tblData.getLines();
	 * int cols = tblData.getFields();
	 * Object[] headings = tblData.getColumnHeader();
	 * String tableName = tblData.getTableName();
	 * String fields = "";
	 * for (int i = 0; i < cols; i++) {
	 * 		if (i == 0) {
	 *			fields += headings[i];
	 *		} else {
	 *			fields += ", " + headings[i];
	 *		}
	 * }
	 * String sqlInsertStatement = getInsertStatement(tableName, fields, sqlType);
	 * 
	 * </pre>
	 */
	private String getInsertStatement(String tableName, String tableFields) {
		int cols = tblData.getFields();
		String valuesMarker = "";
		String insertString = "";
		//Iterates through the columns to get the value placeholders
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				valuesMarker = "?";
			} else {
				valuesMarker += ", ?";
			}
		}
		insertString = "INSERT INTO " + tableName + "(" + tableFields + ") values ("
			+ valuesMarker + ")";
		return insertString;
	}
	
	/**
	 * Directly inserts data from a .csv file into an existing Table for the designated SQL database type
	 * 
	 * @param host - A string where the user can designate the databases file path, defaults to local host 
	 * @param databaseName - A string that specifies the name of the database that the table will be created in
	 * @param sqlType - An enum that specifies which designates what SQL type the database shall be
	 * @param userName - A string that specifies the user name of the creator for SQL access
	 * @param password - A string that specifies the password of the creator for SQL access
	 * @param identity - A boolean that specifies whether to create a default id field
	 * @param idColumn - An integer that specifies which column to be the id column(Cannot be used if identity is true)
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String databaseName = "Example";
	 * String userName = "";
	 * String password = "";
	 * boolean identity = true;
	 * int idColumn = -1;
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * CSVHandler csvHandler = new CSVHandler();
	 * csvHandler.setFirstLineUsedAsColumnHeader(true);
	 * 
	 * TableData tableData = csvHandler.readCSV("TestData/testDataType.csv");
	 * 
	 * SQLHandler sqlHandler = new SQLHandler(tableData);
	 * sqlHandler.insertDatabase(host, databaseName, MYSQL, userName, password, identity, idColumn);
	 * 
	 * </pre>
	 * @throws SQLException
	 * @see TableData
	 * @see CSVHandler
	 */
	public void insertDatabase(String host, String databaseName,
			SQLType sqlType, String userName, String password, boolean identity, int idColumn) throws SQLException{
		final int batchSize = 1000;
		int count = 0;
		Object[][] data = tblData.getTableData();
		int line = 0;
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		Object[] headings = tblData.getColumnHeader();
		String tableName = tblData.getTableName();
		String fields = "";
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				fields += headings[i];
			} else {
				fields += ", " + headings[i];
			}
		}
		String sqlInsertStatement = getInsertStatement(tableName, fields);
		PreparedStatement preparedStatement = null;
		try {
			//If no host has been declared default to local host
			if (host.equalsIgnoreCase("")) {
				host = getHost(sqlType);
				if(sqlType == SQLType.SQLSERVER){
					host += "databaseName=" + databaseName + ";";
				}
			}
			//Creating the connection String used to connect to the database
			connectionURL = getConnectionURL(sqlType, host, databaseName);
			try{
				connection = DriverManager.getConnection(connectionURL, userName, password);
			}catch(SQLException se){
				
			}finally{
				if(connection == null){
					createDatabase(host, databaseName, sqlType, userName, password);
				}	
			}
			connectionURL = getConnectionURL(sqlType, host, databaseName);
			connection = DriverManager.getConnection(connectionURL, userName, password);
			DatabaseMetaData metaData = connection.getMetaData();
			ResultSet resultSet = metaData.getTables(null, null, tableName, null);
			if(!resultSet.next()){
				createTable(host, databaseName, sqlType, userName, password, identity, idColumn);
				connection = DriverManager.getConnection(connectionURL, userName, password);
			}
			//Prepared Statement used to write and execute SQL commands
			preparedStatement = connection.prepareStatement(sqlInsertStatement);
			for (int i = line; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					//Writes the INSERT INTO command
					preparedStatement.setString(j + 1, data[i][j].toString());
				}
				//Adds the INSERT INTO command to a batch awaiting execution
				preparedStatement.addBatch();
				//If the batchSize exceeds 1000 the batch will execute all the INSERT INTO commands
				//that have been added to it. It will then continue to add more INSERT INTO
				//commands until the file has been read, using this 'if' statement should the
				//batchSize exceed 1000 again
				if (count++ % batchSize == 0) {
					preparedStatement.executeBatch();
				}
			}
			//A final execution of any remaining INSERT INTO commands
			preparedStatement.executeBatch();
		} catch (SQLException se) {
			throw(se);
		} finally {
			try {
				//Closes the statement if it was opened
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se) {
				throw(se);
			}
			try {
				//Closes the connection if it was opened
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				throw(se);
			}
		}	
	}
	

	/**
	 * Creates the String for the the designated SQL database type that can then be used to write the .sql file
	 * 
	 * @param databaseName - A string that specifies the name of the database that the table will be created in
	 * @param sqlType - An enum that specifies which designates what SQL type the database shall be
	 * @param identity - A boolean that specifies whether to create a default id field
	 * @param idColumn - An integer that specifies which column to be the id column(Cannot be used if identity is true)
	 * @return SQLFileBuildString - A string that can then be sent to the FileUtilty.writeFile() to create an sql.file
	 * 
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String databaseName = "Example";
	 * 
	 * CSVHandler csvHandler = new CSVHandler();
	 * csvHandler.setFirstLineUsedAsColumnHeader(true);
	 * 
	 * TableData tableData = csvHandler.readCSV("TestData/testDataType.csv");
	 * 
	 * SQLHandler sqlHandler = new SQLHandler(tableData);
	 * sqlHandler.createSQLFile(databaseName, MYSQL, true, -1);
	 * 
	 * FileUtility.writeFile("TestData/test.sql", s);
	 * 
	 * </pre>
	 * @see TableData
	 * @see CSVHandler
	 * @throws Error
	 * @see FileUtility
	 */
	public String createSQLFile(String databaseName, SQLType sqlType, boolean identity, int idColumn) {
		String tableName = tblData.getTableName();
		String fields = getFields();
		String useDatabase = "";
		String insertFields = "";
		String values = "";
		String insertString = "";
		int line = 0;
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		Object[] headings = tblData.getColumnHeader();
		Object[][] data = tblData.getTableData();
		if (sqlType == SQLType.MYSQL) {
			useDatabase = "USE " + databaseName;
		}
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				insertFields += headings[i];
			} else {
				insertFields += ", " + headings[i];
			}
		}
		String idField = "";
		String primaryKey = "";
		if(identity & idColumn != -1){
			throw new Error("Conflict with 'identity' and 'idColumn' paramaters. Both cannot be valid, change 'identity' to false or 'idColumn' to -1");
		}else if(identity){
			idField = getIDField(sqlType) + "\n";
			primaryKey = "id";
		}else if(idColumn > cols || idColumn < 0){
			throw new Error("Conflict with 'idColumn' paramater. 'idColumn' cannot be less than 0 or greater than the columns in the table");
		}else{
			primaryKey += headings[idColumn];
		}
		String createTable = "CREATE TABLE " + tableName + "(\n"
				+ idField + fields + "PRIMARY KEY (" + primaryKey + "));";
		rows = tblData.getLines();
		cols = tblData.getFields();
		for (int i = line; i < rows; i++) {
			values = "";
			for (int j = 0; j < cols; j++) {
				if(j == 0){
					values += ("'" + data[i][j].toString() + "'");
				}else{
					values += (", '" + data[i][j].toString() + "'");
				}
			}
			insertString += "INSERT INTO " + tableName + "(" + insertFields + ") values ("
					+ values + ");\n";
		}
		//Creates a String that can be sent to a file writer to be output as a SQL File
		String SQLFileBuildString = "CREATE DATABASE "
				+ databaseName
				+ ";\n" + useDatabase + "\n"
				+ createTable + ";\n"
				+ insertString + ";";
		return SQLFileBuildString;
	}
}