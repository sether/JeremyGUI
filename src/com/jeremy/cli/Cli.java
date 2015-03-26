package com.jeremy.cli;

import com.jeremy.*;
import com.jeremy.FileController.OutputType;
import com.jeremy.SQLHandler.SQLType;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Cli {
	private FileController fc = null;
	private SQLHandler sql;
	private File csvFile = null;
	private int userResponse = 0;
	private Scanner scan;
	private String filePath = "";
	private String userString = "";
	
	
	private String[] columnClasses = null;
	
	public Cli() {
		scan = new Scanner(System.in);
		fc = new FileController();	
	}
	 
	public void begin() {
		gatherFile();
		csvHandling();
		tableDataHandling();
		fileExporting();
	}

	private void gatherFile() {
		System.out.println("Welcome to the Jeremy CSV API command line interface.\nFirst, we'll need you to enter the filepath of your .csv file.\nExample: C:\\folder\\data.csv");
		filePath = scan.next();
		while (!checkCsv(filePath)) {
			System.out.println("Enter a valid path to your csv file. (Type \"exit\" to quit");
			filePath = scan.next();
			if (filePath.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
		}		
	}
	
	private void csvHandling() {
		while (true) {
			System.out.println("Please pick the relevant setup options to your file:");
			System.out.println("1: First row as headings? Currently:" + fc.isFirstLineUsedAsColumnHeader());
			System.out.println("2: Does your CSV file use a different delimiter character for columns? (Default = \",\")");
			System.out.println("3: Do you require a specific date format? (Default = \"dd/MM/yyyy\")");
			System.out.println("4: Move on to Table Data configuration");

			userResponse = scan.nextInt();
			
			if (userResponse == 1) {
				while (true) {	
					System.out.println("Set first row as headings - (Enter: True/False)");
					userString = scan.next();
					userResponse = parseInput(userString);
					
					if (userResponse == 1) {
						fc.setFirstLineUsedAsColumnHeader(true);
						System.out.println("First row set as column headings.");
						break;
					} else if (userResponse == 0){
						fc.setFirstLineUsedAsColumnHeader(false);
						System.out.println("First row NOT set as column headings.");
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}

			} else if (userResponse == 2) {
				while (true) {
					System.out.println("Change column delimiter? (Enter: Yes/No)");
					userString = scan.next();
					userResponse = parseInput(userString);
					
					if (userResponse == 1) {
						System.out.println("Enter a new delimiter: E.g - \".\", \"|\", etc.");
						String newDelimiter = scan.next();
						fc.setColumnDelimiter(newDelimiter);
						break;
					} else if (userResponse == 0){
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}
				
			} else if (userResponse == 3) {
				while (true) {
					System.out.println("Enter a new date format? (Enter: Yes/No)");
					userString = scan.next();
					userResponse = parseInput(userString);
					
					if (userResponse == 1) {
						System.out.println("Enter your new date format. E.g - \"yyyy/MM/dd\"");
						String newDateFormat = scan.next();
						fc.setDateFormat(newDateFormat);
						break;
					} else if (userResponse == 0){
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}
				
			} else if (userResponse == 4) { 
				try {
					fc.readFile(filePath);
				} catch (IOException e) {
					System.out.println("A file error has occurred");
					System.exit(0);
				}
				// Move on to next method call.
				break;
			}
		}
	}

	private void tableDataHandling() {
		createColumnClasses();

		while (true) {
			System.out.println("Please pick the relevant table data options for your file:");
			System.out.println("NOTE: Please check the column class data types are correct if you require them later.");
			System.out.println("1: Check column class data type information.");
			System.out.println("2: Edit table name. Currently: " + fc.getTableName());
			System.out.println("3: Continue");
			
			userResponse = scan.nextInt();
			if (userResponse == 1) {
				while (true) {	
					System.out.println("Current column classes are as follows:");
					
					printColumnsClasses();
					
					System.out.println("Is this correct?");
					userString = scan.next();
					userResponse = parseInput(userString);
					if (userResponse == 1) {
						break;
					} else if (userResponse == 0) {
						System.out.println("OK, which index is incorrect?");
						int index = scan.nextInt();
						System.out.println("OK, what data type do you wish to change to? (String, Integer, Date, Boolean, etc)");
						String dataType = scan.next();
						setColumnClass(index, dataType);
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}
			} else if (userResponse == 2) {
				System.out.println("Enter new table name");
				userString = scan.next();
				fc.setTableName(userString);
			} else if (userResponse == 3) {
				break;
			}
		}
	}
	
	private void fileExporting() {
		
		while (true) {
			System.out.println("Please pick the relevant exporting options for your file:");
			System.out.println("1: Export to JSON format.");
			System.out.println("2: Export to XML. (XML, XML SCHEMA)");
			System.out.println("3: Export to SQL. (Postgre, MSSQL, MySQL)");
			System.out.println("4: Exit Program");
			
			userResponse = scan.nextInt();
			if (userResponse == 1) {				
				System.out.println("Enter the destination directory of the file.");
				System.out.println("Example: \"C:\\test\\");
				String destination = scan.next();
				try {
					fc.outputData(destination, fc.getTableName() + ".json", OutputType.JSON);
					System.out.println(destination + "\\" + fc.getTableName() + ".JSON written to disk.");
					continue;
				} catch (IOException e) {
					System.out.println("A disk read error has occurred. Exiting...");
				}
									
			} else if (userResponse == 2) {
				System.out.println("Enter the destination directory of the file.");
				System.out.println("Example: \"C:\\test\\");
				String destination = scan.next();
				try {
					fc.outputData(destination, fc.getTableName() + ".xml", OutputType.XML);
					fc.outputData(destination, fc.getTableName() + ".xsd", OutputType.XML_SCHEMA);
					System.out.println(destination + "\"" + fc.getTableName() + ".XML written to disk.");
					System.out.println(destination + "\"" + fc.getTableName() + ".XSD written to disk.");
					continue;
				} catch (IOException e) {
					System.out.println("A disk error has occurred. Exiting...");
					System.exit(0);
				}
			} else if (userResponse == 3) {
				System.out.println("First, Which DBMS will you be exporting to?");
				System.out.println("1. MySQL");
				System.out.println("2. MSSQL");
				System.out.println("3. Postgre");						
				
				userResponse = scan.nextInt();
				while(true) {
					if (userResponse == 1) {
						gatherDatabaseInfo(SQLType.MYSQL);
						writeDatabaseFile(SQLType.MYSQL);

					} else if (userResponse == 2) {
						gatherDatabaseInfo(SQLType.SQLSERVER);
						writeDatabaseFile(SQLType.SQLSERVER);
						break;
					} else if (userResponse == 3) {
						gatherDatabaseInfo(SQLType.POSTGRESQL);
						writeDatabaseFile(SQLType.POSTGRESQL);
						break;
					} else {
						System.out.println("Enter a valid value");
						continue;
					}
				}
			} else if (userResponse == 4) {
				System.exit(0);
			} else {
				System.out.println("Enter a valid option.");
				continue;
			}
		}
	}
	
	private void gatherDatabaseInfo(SQLType type) {
		System.out.println("Enter the database hostname.");
		System.out.println("Example: localhost");
		String host = scan.next();
		
		System.out.println("Enter the database port number.");
		String port = scan.next();
		
		System.out.println("Enter the database username.");
		String userName = scan.next();
		
		System.out.println("Enter the database password.");
		
		String password = scan.nextLine();
		if (scan.nextLine().isEmpty()) {
			// You should really use a password
			password = "";
		}
		
		try {
			fc.outputToDatabase(host, port, fc.getTableName(), type, userName, password, true, -1);
			System.out.println("Database created successfully.");
			
		} catch (SQLException e) {
			System.out.println("A database error occured, have you checked the hostname/password/username/password has been correctly entered?");
		}
	}
	
	private void writeDatabaseFile(SQLType type) {
		System.out.println("Enter the destination directory of the sql file.");
		System.out.println("Example: \"C:\\test\\");
		String destination = scan.next();
		
		try {
			fc.outputToSQLFile(new File(destination, fc.getTableName() + ".sql"), fc.getTableName(), type, true, -1);
			System.out.println(destination + "\\" + fc.getTableName() + ".SQL written to disk.");
		} catch (IOException e) {
			System.out.println("A disk error has occurred. Exiting...");
			System.exit(0);
		}
	}
	
 	private int parseInput(String response) {
		if (response.equalsIgnoreCase("true") || (response.equalsIgnoreCase("yes"))) {
			return 1;
		} else if (response.equalsIgnoreCase("false") || (response.equalsIgnoreCase("no"))) {
			return 0;
		} else
			return -1;	
	}
	
	private void setColumnClass(int index, String dataType) {
		columnClasses[index] = dataType;
	}

	private void createColumnClasses() {
		columnClasses = new String[fc.getFields()];
		for (int i = 0; i < fc.getFields(); i++) {
			columnClasses[i] = fc.getColumnClasses()[i].getSimpleName();
		}
	}
	
	private void printColumnsClasses() {
		for (int i = 0; i < fc.getFields(); i++) {
				System.out.println("Column " + i + ": " + columnClasses[i]);
		}
	}
	
	private boolean checkCsv(String filePath) {
		csvFile = new File(filePath);
		if (!csvFile.isFile()) {
			return false;
		} 
		return true;
	}
	
	public static void main(String[] args) {
		Cli cli = new Cli();
		cli.begin();
	}
}

