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
	private String driver = ""; //"com.mysql.jdbc.Driver"
	private String URL;
	private String userName;
	private String password;
	private Connection connection;
	private Statement statement;

	public SQLConnection(String databaseURL, SQLType sqlType) {
		this(databaseURL, "", "", sqlType);
	}

	public SQLConnection(String databaseURL, String userName, SQLType sqlType) {
		this(databaseURL, userName, "", sqlType);
	}

	public SQLConnection(String databaseURL, String userName, String password, SQLType sqlType) {
		URL = databaseURL;
		this.userName = userName;
		this.password = password;

		// set up the driver
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

	public Connection open() {
		// open the connection to the database and create a statement
		try {
			connection = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			System.out.println("Could not open database: " + e);
		}
		return connection;
	}

	public void close() {
		// close both the statement and the connection
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close database: " + e);
		}
	}

	public void resetConnection() {
		// resets the connection and the statement
		close();
		open();
	}

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
