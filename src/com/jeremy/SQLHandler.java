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
	private static String connectionURL = "jdbc:mysql://localhost/";

	//Gets the TableData
	public SQLHandler(TableData data) {
		tblData = data;
	}

	//Creates the Database for the SQL File
	public void createDatabase(String databaseName) {
		final String USER = "username";
		final String PASS = "password";
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionURL, USER, PASS);
			statement = connection.createStatement();
			String createDatabase = "CREATE DATABASE" + databaseName;
			statement.executeUpdate(createDatabase);
		} catch (SQLException se) {
			//Catches errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Catches errors for Class.forName
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

	//Creates a Table for the Database
	public void createTable(String tableName, String databaseName) {
		Object[][] data = tblData.getTableData();
		final String USER = "username";
		final String PASS = "password";
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionURL + databaseName, USER, PASS);
			statement = connection.createStatement();
			String fields = "";
			String dataType = " VARCHAR(255), ";
			int cols = tblData.getFields();
			for (int i = 0; i < cols; i++) {
				fields += data[0][i].toString() + dataType;
			}
			String createTable = "CREATE TABLE" + tableName + " " +
	                   "(id INTEGER not NULL, " + fields + " PRIMARY KEY (id))"; 

			statement.executeUpdate(createTable);
		} catch (SQLException se) {
			//Catches errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Catches errors for Class.forName
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
	
	//Inserts data into the Database
	public void createSQLFile(String tableName, String tableFields) throws SQLException {
				final int batchSize = 1000;
		int count = 0;
		Object[][] data = tblData.getTableData();
		int line = 1;
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		int marker = tblData.getFields();
		String valuesMarker = "";
		for(int i = 0; i < marker; i++){
			if(i == 0){
				valuesMarker = "?";	
			}else{
				valuesMarker += ", ?";
			}
		}
		String sql = "INSERT INTO " + "(" + tableFields + ") values (" + valuesMarker + ")";
		Connection connection = DriverManager.getConnection(connectionURL, "root", "");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
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
}