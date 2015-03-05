package com.jeremy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Class to convert a TableData object into a SQL file
 * 
 * @author Ryan Kavanagh
 * @version 1.0
 */
public class SQLHandler {
	private TableData tblData;
	private Connection connection = null;
	private String USER = "username";
	private String PASSWORD = "password";
	private String driver = "";

	public enum SQLType {
		SQLSERVER, MYSQL, POSTGRESQL
	};

	private static String connectionURL = "";

	// Gets the TableData
	public SQLHandler(TableData data) {
		tblData = data;
	}

	// Creates the Database for the SQL File
	public void createDatabase(String host, String databaseName,
			SQLType sqlType, String userName, String password) {
		USER = userName;
		PASSWORD = password;
		Connection connection = null;
		Statement statement = null;
		try {
			if (host.equalsIgnoreCase("")) {
				if (sqlType == SQLType.SQLSERVER) {
					host = "localhost:1433/";
				} else if (sqlType == SQLType.MYSQL) {
					host = "localhost:3306/";
				} else if (sqlType == SQLType.POSTGRESQL) {
					host = "localhost:5432/";
				}
			}
			if (sqlType == SQLType.SQLSERVER) {
				connectionURL = "jdbc:microsoft:sqlserver://" + host
						+ databaseName;
			} else if (sqlType == SQLType.MYSQL) {
				connectionURL = "jdbc:mysql://" + host + databaseName;
			} else if (sqlType == SQLType.POSTGRESQL) {
				connectionURL = "jdbc:postgresql://" + host + databaseName;
			}
			SQLConnection sqlConnection = new SQLConnection(connectionURL,
					USER, PASSWORD, sqlType);
			connection = sqlConnection.open();
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
				se2.printStackTrace();
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
	public void createTable(String host, String databaseName,
			SQLType sqlType, String userName, String password) {
		USER = userName;
		PASSWORD = password;
		Object[][] data = tblData.getTableData();
		Statement statement = null;
		try {
			if (host.equalsIgnoreCase("")) {
				if (sqlType == SQLType.SQLSERVER) {
					host = "localhost:1433/";
				} else if (sqlType == SQLType.MYSQL) {
					host = "localhost:3306/";
				} else if (sqlType == SQLType.POSTGRESQL) {
					host = "localhost:5432/";
				}
			}
			if (sqlType == SQLType.SQLSERVER) {
				connectionURL = "jdbc:microsoft:sqlserver://" + host
						+ databaseName;
			} else if (sqlType == SQLType.MYSQL) {
				connectionURL = "jdbc:mysql://" + host + databaseName;
			} else if (sqlType == SQLType.POSTGRESQL) {
				connectionURL = "jdbc:postgresql://" + host + databaseName;
			}
			SQLConnection sqlConnection = new SQLConnection(connectionURL,
					USER, PASSWORD, sqlType);
			connection = sqlConnection.open();
			statement = connection.createStatement();
			String fields = "";
			String dataType = " VARCHAR(255), ";
			int cols = tblData.getFields();
			Class<?>[] columnClasses = tblData.getColumnClasses();
			Object[] headings = tblData.getColumnHeader();
			String tableName = tblData.getTableName();
			for (int i = 0; i < cols; i++) {
				if(columnClasses[i] == Boolean.class){
					dataType = "BOOLEAN, ";
				}else if(columnClasses[i] == Integer.class){
					dataType = "INTEGER(" + tblData.getFieldLength()[i] + "), ";
				}else if(columnClasses[i] == Double.class){
					dataType = "DECIMAL(" + tblData.getFieldLength()[i] + "," + tblData.getFieldPrecision()[i] + "), ";
				}else if(columnClasses[i] == Date.class){
					dataType = "DATE, ";
				}else if(columnClasses[i] == Long.class){
					dataType = "BIGINT, ";
				}else{
					dataType = "VARCHAR(" + tblData.getFieldLength()[i] + "), ";
				}
			}
			for (int i = 0; i < cols; i++) {
				fields += headings[i] + " " + dataType;
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
				se2.printStackTrace();
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
	private String getInsertStatement(String tableName, String tableFields) {
		int cols = tblData.getFields();
		String valuesMarker = "";
		for (int i = 0; i < cols; i++) {
			if (i == 0) {
				valuesMarker = "?";
			} else {
				valuesMarker += ", ?";
			}
		}
		return "INSERT INTO " + tableName + "(" + tableFields + ") values (" + valuesMarker
				+ ")";
	}

	// Inserts data into the Database using a pre-made insert statement
	public void insertDatabase(String host, String databaseName, SQLType sqlType,
			String userName, String password) throws SQLException {
		final int batchSize = 1000;
		int count = 0;
		Object[][] data = tblData.getTableData();
		int line = 1;
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		Object[] headings = tblData.getColumnHeader();
		String tableName = tblData.getTableName();
		String fields = "";
		for (int i = 0; i < cols; i++) {
			if(i == 0){
				fields += headings[i];
			}else{
				fields += ", " + headings[i];
			}
		}
		String sqlInsertStatement = getInsertStatement(tableName, fields);
		PreparedStatement preparedStatement = null;
		try {
			if (host.equalsIgnoreCase("")) {
				if (sqlType == SQLType.SQLSERVER) {
					host = "localhost:1433/";
				} else if (sqlType == SQLType.MYSQL) {
					host = "localhost:3306/";
				} else if (sqlType == SQLType.POSTGRESQL) {
					host = "localhost:5432/";
				}
			}
			if (sqlType == SQLType.SQLSERVER) {
				connectionURL = "jdbc:microsoft:sqlserver://" + host
						+ databaseName;
			} else if (sqlType == SQLType.MYSQL) {
				connectionURL = "jdbc:mysql://" + host + databaseName;
			} else if (sqlType == SQLType.POSTGRESQL) {
				connectionURL = "jdbc:postgresql://" + host + databaseName;
			}
			SQLConnection sqlConnection = new SQLConnection(connectionURL,
					USER, PASSWORD, sqlType);
			connection = sqlConnection.open();
			preparedStatement = connection.prepareStatement(sqlInsertStatement);
			for (int i = line; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					preparedStatement.setString(j + 1, data[i][j].toString());
				}
				preparedStatement.addBatch();
				if (count++ % batchSize == 0) {
					preparedStatement.executeBatch();
				}
				preparedStatement.executeBatch();
			}
		} catch (SQLException se) {
			// Catches errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Catches errors for Class.forName
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}

		// preparedStatement.close();
		// connection.close();
	}

	// Creates the string needed for the SQL file
	public String createSQLFile(String databaseName) {
		Class<?>[] columnClasses = tblData.getColumnClasses();
		Object[] headings = tblData.getColumnHeader();
		String tableName = tblData.getTableName();
		String fields = "";
		String dataType = "";
		int cols = tblData.getFields();
		for (int i = 0; i < cols; i++) {
			if(columnClasses[i] == Boolean.class){
				dataType = "BOOLEAN, ";
			}else if(columnClasses[i] == Integer.class){
				dataType = "INTEGER(" + tblData.getFieldLength()[i] + "), ";
			}else if(columnClasses[i] == Double.class){
				dataType = "DECIMAL(" + tblData.getFieldLength()[i] + "," + tblData.getFieldPrecision()[i] + "), ";
			}else if(columnClasses[i] == Date.class){
				dataType = "DATE, ";
			}else if(columnClasses[i] == Long.class){
				dataType = "BIGINT, ";
			}else{
				dataType = "VARCHAR(" + tblData.getFieldLength()[i] + "), ";
			}
			fields += headings[i] + " " + dataType;
		}
		String createTable = "CREATE TABLE " + tableName + " ("
				+ "id INTEGER not NULL, " + fields + " PRIMARY KEY (id))";
		String SQLFileBuildString = "USE master;\n" + "GO\n"
				+ "IF EXISTS(SELECT * FROM sysdatabases WHERE name= '"
				+ databaseName
				+ "')\n"
				+ "BEGIN\n"
				+ "RAISERROR('Dropping existing "
				+ databaseName
				+ " database ....',0,1)\n"
				+ "DROP database "
				+ databaseName
				+ ";"
				+ "\nEND\n"
				+ "GO\n"
				+ "RAISERROR('Creating "
				+ databaseName
				+ " database ....',0,1)\n"
				+ "GO\n"
				+ "CREATE DATABASE "
				+ databaseName
				+ ";\n"
				+ "GO\n"
				+ "USE "
				+ databaseName
				+ ";\n"
				+ "GO\n"
				+ "RAISERROR('Creating Tables ....',0,1)\n"
				+ "GO\n"
				+ createTable + ";";
		return SQLFileBuildString;
	}

	// Use Alex's getters and setter

	// Acquire jar files
	// Install all database management system
	// connectionstrings.com (Helpful)
}