package com.jeremy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to convert a TableData object into a SQL file
 * @author Ryan Kavanagh
 * @version 1.0
 */
public class SQLHandler {
	private TableData tblData;
	public enum SQLType{
		SQLSERVER,
		MYSQL,
		POSTGRESQL
	};
	private static String connectionURL = "jdbc:mysql://localhost/";
	SQLConnection SQLDatabase = new SQLConnection("jdbc:mysql://localhost:3306/Video_C4PRG", "root");

	// Gets the TableData
	public SQLHandler(TableData data) {
		tblData = data;
	}

	// Creates the Database for the SQL File
	public void createDatabase(String databaseName, SQLType sqlType) {
		final String USER = "username";
		final String PASS = "password";
		Connection connection = null;
		Statement statement = null;
		try {
			if(sqlType == SQLType.SQLSERVER){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(connectionURL, USER, PASS);
			}else if(sqlType == SQLType.MYSQL){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(connectionURL, USER, PASS);
			}else if(sqlType == SQLType.POSTGRESQL){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(connectionURL, USER, PASS);
			}
			statement = connection.createStatement();
			String createDatabase = "CREATE DATABASE" + databaseName;
			statement.executeUpdate(createDatabase);
		} catch (SQLException se) {
			// Catches errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Catches errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {

			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	// Creates a Table for the Database
	public void createTable(String tableName, String databaseName, SQLType sqlType) {
		Object[][] data = tblData.getTableData();
		final String USER = "username";
		final String PASS = "password";
		Connection connection = null;
		Statement statement = null;
		try {
			if(sqlType == SQLType.SQLSERVER){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(connectionURL, USER, PASS);
			}else if(sqlType == SQLType.MYSQL){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(connectionURL, USER, PASS);
			}else if(sqlType == SQLType.POSTGRESQL){
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(connectionURL, USER, PASS);
			}
			statement = connection.createStatement();
			String fields = "";
			String dataType = " VARCHAR(255), ";
			int cols = tblData.getFields();
			for (int i = 0; i < cols; i++) {
				fields += data[0][i].toString() + dataType;
			}
			String createTable = "CREATE TABLE" + tableName + " ("
					+ "(id INTEGER not NULL, " + fields + " PRIMARY KEY (id))";
			statement.executeUpdate(createTable);
		} catch (SQLException se) {
			// Catches errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Catches errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (SQLException se2) {

			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	// Creates the insert statement for the Database
	private String getInsertStatement(String tableFields) {
		int cols = tblData.getFields();
		String valuesMarker = "";
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				valuesMarker = "?";
			} else {
				valuesMarker += ", ?";
			}
		}
		return "INSERT INTO " + "(" + tableFields + ") values (" + valuesMarker	+ ")";
	}

	// Inserts data into the Database using a pre-made insert statement
	public void insertDatabase(String tableName, String tableFields, String databaseName, SQLType sqlType) throws SQLException {
		final int batchSize = 1000;
		int count = 0;
		Object[][] data = tblData.getTableData();
		int line = 1;
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		String sqlInsertStatement = getInsertStatement(tableFields);
		Connection connection = null;
		if(sqlType == SQLType.SQLSERVER){
			connection = DriverManager.getConnection(connectionURL);
		}else if(sqlType == SQLType.MYSQL){
			connection = DriverManager.getConnection(connectionURL);
		}else if(sqlType == SQLType.POSTGRESQL){
			connection = DriverManager.getConnection(connectionURL);
		}
		PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertStatement);
		for (int i = line; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				preparedStatement.setString(j + 1, data[i][j].toString());
			}
			preparedStatement.addBatch();
			if (count++ % batchSize == 0) {
				preparedStatement.executeBatch();
			}
		}
		preparedStatement.executeBatch();
		preparedStatement.close();
		connection.close();
	}

	// Creates the string needed for the SQL file
	public String createSQLFile(String tableName, String databaseName) {
		Object[][] data = tblData.getTableData();
		String fields = "";
		String dataType = " VARCHAR(255), ";
		int cols = tblData.getFields();
		for (int i = 0; i < cols; i++) {
			fields += data[0][i].toString() + dataType;
		}
		String createTable = "CREATE TABLE" + tableName + " ("
				+ "(id INTEGER not NULL, " + fields + " PRIMARY KEY (id))";
		String SQLFileBuildString = "USE master" + "GO"
				+ "IF EXISTS(SELECT * FROM sysdatabases WHERE name='"
				+ databaseName
				+ "')"
				+ "BEGIN"
				+ "RAISERROR('Dropping existing "
				+ databaseName
				+ " database ....',0,1)"
				+ "DROP database "
				+ databaseName
				+ "END"
				+ "GO"
				+ "RAISERROR('Creating "
				+ databaseName
				+ " database ....',0,1)"
				+ "GO"
				+ "CREATE DATABASE "
				+ databaseName
				+ ";"
				+ "GO"
				+ "USE "
				+ databaseName
				+ ";"
				+ "GO"
				+ "RAISERROR('Creating Tables ....',0,1)"
				+ "GO"
				+ createTable;
		return SQLFileBuildString;
	}

//Research preparedStatements confirm that all the database management systems can use
//Create connection class
//Create Login for each database management system
//Acquire jar files
//Figure out the tableData .csv data type for tables
//Install all database management system
//connectionstrings.com
}