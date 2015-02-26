package com.jeremy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class to create a connection to a SQL Database
 * @author Ryan Kavanagh
 * @version 1.0
 */
public class SQLConnection {
	private boolean debugMode = false;

	private String driver = "com.mysql.jdbc.Driver";
	
	private String URL;
	private String userName;
	private String password;
	private Connection connection;
	private Statement statement;

	public SQLConnection(String databaseURL) {
		this(databaseURL, "", "");
	}

	public SQLConnection(String databaseURL, String userName) {
		this(databaseURL, userName, "");
	}

	public SQLConnection(String databaseURL, String userName, String password) {
		URL = databaseURL;
		this.userName = userName;
		this.password = password;

		//set up the driver
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void open() {
		//open the connection to the database and create a statement
		try {
			connection = DriverManager.getConnection(URL, userName, password);
		} catch (SQLException e) {
			System.out.println("Could not open database: " + e);
		}
	}

	public void close() {
		//close both the statement and the connection
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close database: " + e);
		}
	}

	public void resetConnection() {
		//resets the connection and the statement
		close();
		open();
	}
	
	public void setDriver(String driver){
		this.driver = driver;
	}
}
