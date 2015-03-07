package com.jeremy;

import java.sql.Connection;

import com.jeremy.SQLHandler.SQLType;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Class to create a connection to a SQL Database
 * 
 * @author Ryan Kavanagh
 * @version 1.0
 */
public class SQLConnection {
	private String driver = "";
	private String URL;
	private String userName;
	private String password;
	private Connection connection;
	private Statement statement;

	/**
	 * The class's constructor that connects to a designated sql database
	 * 
	 * @param databaseURL - A string where the user can designate the databases file path, defaults to local host 
	 * @param userName - A string that specifies the user name of the creator for SQL access
	 * @param password - A string that specifies the password of the creator for SQL access
	 * @param sqlType - An enum that specifies which designates what SQL type the database shall be
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * SQLConnection sqlConnection = new SQLConnection(host, userName, password, MYSQL);
	 * </pre>
	 */
	public SQLConnection(String databaseURL, String userName, String password, SQLType sqlType) {
		URL = databaseURL;
		this.userName = userName;
		this.password = password;
		try {
			if(sqlType == SQLType.SQLSERVER){
				driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
			}else if(sqlType == SQLType.MYSQL){
				driver = "com.mysql.jdbc.Driver";
			}else if(sqlType == SQLType.POSTGRESQL){
				driver = "org.postgresql.Driver";
			}
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to create a connection to a sql database
	 * 
	 * @return connection - the conenction to the database is returned
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * SQLConnection sqlConnection = new SQLConnection(host, userName, password, MYSQL);
	 * sqlConnection.open();
	 * </pre>
	 */
	public Connection open() {
		try {
			connection = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			System.out.println("Could not open database: " + e);
		}
		return connection;
	}

	/**
	 * Used to close an existing connection to a sql database
	 * 
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * SQLConnection sqlConnection = new SQLConnection(host, userName, password, MYSQL);
	 * sqlConnection.open();
	 * sqlConnection.close();
	 * </pre>
	 */
	public void close() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close database: " + e);
		}
	}

	/**
	 * Used to reset an existing connection to a sql database
	 * 
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * SQLConnection sqlConnection = new SQLConnection(host, userName, password, MYSQL);
	 * sqlConnection.reset();
	 * </pre>
	 */
	public void resetConnection() {
		close();
		open();
	}

	/**
	 * Used to change the driver that an open SQLConnection is connecting through to an existing connection to a sql database
	 * 
	 * @param sqlType - An enum that specifies which designates what SQL type the database shall be
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * String host = "localhost:1433";
	 * String userName = "";
	 * String password = "";
	 * private enum SQLType {
	 * 		SQLSERVER, MYSQL, POSTGRESQL
	 * };
	 * 
	 * SQLConnection sqlConnection = new SQLConnection(host, userName, password, MYSQL);
	 * sqlConnection.setDriver(SQLSERVER);
	 * </pre>
	 */
	public void setDriver(SQLType sqlType) {
		if(sqlType == SQLType.SQLSERVER){
			this.driver = "";
		}else if(sqlType == SQLType.MYSQL){
			this.driver = "com.mysql.jdbc.Driver";
		}else if(sqlType == SQLType.POSTGRESQL){
			this.driver = "";
		}
	}
}
