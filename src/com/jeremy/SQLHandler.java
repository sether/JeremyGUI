package com.jeremy;

import java.sql.Connection;
import java.sql.DriverManager;
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
	public void createDatabase(String host, String databaseName,
			SQLType sqlType, String userName, String password) {
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
				connectionURL = "jdbc:mysql://" + host;
			} else if (sqlType == SQLType.POSTGRESQL) {
				connectionURL = "jdbc:postgresql://" + host;
			}
			connection = DriverManager.getConnection(connectionURL, userName, password);
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

	/**
	 * Directly creates a Table for the designated SQL database type
	 * 
	 * @param host - A string where the user can designate the databases file path, defaults to local host 
	 * @param databaseName - A string that specifies the name of the database that the table will be created in
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
	 * sqlHandler.createTable(host, databaseName, MYSQL, userName, password);
	 * 
	 * </pre>
	 * @throws SQLException
	 * @see TableData
	 * @see CSVHandler
	 */
	public void createTable(String host, String databaseName, SQLType sqlType,
			String userName, String password) {
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
			connection = DriverManager.getConnection(connectionURL, userName, password);
			statement = connection.createStatement();
			String fields = "";
			String dataType = " VARCHAR(255), ";
			int cols = tblData.getFields();
			Class<?>[] columnClasses = tblData.getColumnClasses();
			Object[] headings = tblData.getColumnHeader();
			String tableName = tblData.getTableName();
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
			System.out.println(fields);
			String createTable = "CREATE TABLE " + tableName + " ("
					+ "(id INT NOT NULL AUTO_INCREMENT, \n" + fields + " PRIMARY KEY (id))";
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
	 * String sqlInsertStatement = getInsertStatement(tableName, fields);
	 * 
	 * </pre>
	 */
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
		return "INSERT INTO " + tableName + "(" + tableFields + ") values ("
				+ valuesMarker + ")";
	}

	/**
	 * Directly inserts data from a .csv file into an existing Table for the designated SQL database type
	 * 
	 * @param host - A string where the user can designate the databases file path, defaults to local host 
	 * @param databaseName - A string that specifies the name of the database that the table will be created in
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
	 * sqlHandler.insertDatabase(host, databaseName, MYSQL, userName, password);
	 * 
	 * </pre>
	 * @throws SQLException
	 * @see TableData
	 * @see CSVHandler
	 */
	public void insertDatabase(String host, String databaseName,
			SQLType sqlType, String userName, String password)
			throws SQLException {
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
			if (i == 0) {
				fields += headings[i];
			} else {
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
			connection = DriverManager.getConnection(connectionURL, userName, password);
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
	}

	/**
	 * Creates the String for the the designated SQL database type that can then be used to write the .sql file
	 * 
	 * @param databaseName - A string that specifies the name of the database that the table will be created in
	 * @return SQLFileBuildString - A string that can then be sent to the FileUtilty.writeFile() to create an sql.file
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
	 * sqlHandler.createSQLFile(databaseName);
	 * 
	 * FileUtility.writeFile("TestData/test.sql", s);
	 * 
	 * </pre>
	 * @see TableData
	 * @see CSVHandler
	 * @see FileUtility
	 */
	public String createSQLFile(String databaseName) {
		Class<?>[] columnClasses = tblData.getColumnClasses();
		Object[] headings = tblData.getColumnHeader();
		String tableName = tblData.getTableName();
		String fields = "";
		String dataType = "";
		int cols = tblData.getFields();
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
			fields += headings[i] + " " + dataType;
		}
		String createTable = "CREATE TABLE " + tableName + " ("
				+ "id INTEGER not NULL, " + fields + " PRIMARY KEY (id))";
		String SQLFileBuildString = "USE master;\n"
				+ "IF EXISTS(SELECT * FROM sysdatabases WHERE name= '"
				+ databaseName
				+ "')\n"
				+ "BEGIN\n"
				+ databaseName
				+ " database ....',0,1)\n"
				+ "DROP database "
				+ databaseName
				+ ";"
				+ "\nEND\n"
				+ databaseName
				+ " database ....',0,1)\n"
				+ "CREATE DATABASE "
				+ databaseName
				+ ";\n"
				+ "USE "
				+ databaseName
				+ ";\n"
				+ createTable + ";";
		return SQLFileBuildString;
	}
}