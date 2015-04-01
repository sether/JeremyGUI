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
	private File csvFile = null;
	private int userResponse = 0;
	private Scanner scan;
	private String filePath = "";
	private String userString = "";
	private String[] columnClasses = null;
	private final String DESTINATION_INFO = "Enter the destination directory of the file.\nExample: \"C:\\test\\";
	private final String FILE_EXPORT_INTRO = "Please pick the relevant exporting options for your file:\n1: Export to JSON format.\n2: Export to XML. (XML, XML SCHEMA)\n3: Export to SQL. (Postgre, MSSQL, MySQL)\n4: Exit Program.";
	private final String IO_ERROR = "A disk read error has occurred. Exiting...";
	private final String INVALID_INPUT = "Enter a valid option.";
	
	
	// The default and only constructor for the CLI, it instantiates the scanner to get user input and the jeremy file controller so it can begin taking in data.
	public Cli() {
		scan = new Scanner(System.in);
		fc = new FileController();	
	}
	 
	private void begin() {
		gatherFile();
		csvHandling();
		tableDataHandling();
		fileExporting();
	}
	
	// Here we start the CLI, we gather the file, make sure it's usable and we move on.
	private void gatherFile() {
		final String WELCOME = "Welcome to the Jeremy CSV API command line interface.\nFirst, we'll need you to enter the filepath of your .csv file.\nExample: C:\\folder\\data.csv";
		final String INVALID_PATH = "Enter a valid path to your csv file. (Type \"exit\" to quit";
		System.out.println(WELCOME);
		filePath = scan.next();
		while (!checkCsv(filePath)) {
			System.out.println(INVALID_PATH);
			filePath = scan.next();
			if (filePath.equalsIgnoreCase("exit")) {
				System.exit(0);
			}
		}		
	}
	
	// Here we handle the CSV portion of the data, this includes settings row headings, changing delimiters and date formats.
	// after the user is finished with the csv file, we move on the the table data options.
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
						System.out.println(INVALID_INPUT);
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
						// Set the delimiter then reload the data
						fc.setColumnDelimiter(newDelimiter);
						try {
							fc.readFile(filePath);
						} catch (IOException e) {
							System.out.println(IO_ERROR);
							System.exit(0);
						}
						break;
					} else if (userResponse == 0){
						break;
					} else {
						System.out.println(INVALID_INPUT);
						continue;
					}
				}
				
			} else if (userResponse == 3) {
				final String DATE_INFO = "Enter a new date format? (Enter: Yes/No)";
				final String ENTER_DATE = "Enter your new date format. E.g - \"yyyy/MM/dd\"";
				while (true) {
					System.out.println(DATE_INFO);
					userString = scan.next();
					userResponse = parseInput(userString);
					
					if (userResponse == 1) {
						System.out.println(ENTER_DATE);
						String newDateFormat = scan.next();
						fc.setDateFormat(newDateFormat);
						break;
					} else if (userResponse == 0){
						break;
					} else {
						System.out.println(INVALID_INPUT);
						continue;
					}
				}
				
			} else if (userResponse == 4) { 
				try {
					fc.readFile(filePath);
				} catch (IOException e) {
					System.out.println(IO_ERROR);
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
						System.out.println("Which index would you like to change?");
						int index = scan.nextInt();
						if (index > columnClasses.length - 1 || index < 0) {
							System.out.println("Enter a valid index");
							continue;
						}
						System.out.println("What data type do you wish to change to?\nUse a qualified class name E.g - (java.lang.String, java.util.Date, java.lang.boolean, etc");
						String dataType = scan.next();
						try {
							setColumnClass(index, dataType);
							System.out.println("Column class set.");
						} catch (ClassNotFoundException e) {
							System.out.println("Enter a valid class.");
							continue;
						}
						break;
					} else {
						System.out.println(INVALID_INPUT);
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
	
	// Here we handle the exporting of data to JSON,XML, etc. and also writing database files and also pushing to databases directly.
	private void fileExporting() {	
		final String DBMS_INFO = "First, Which DBMS will you be exporting to?\n1. MySQL.\n2. MSSQL.\n3. Postgre.";
		
		while (true) {
			System.out.println(FILE_EXPORT_INTRO);
			
			userResponse = scan.nextInt();
			if (userResponse == 1) {				
				System.out.println(DESTINATION_INFO);
	
				String destination = scan.next();
				try {
					fc.outputData(destination, fc.getTableName() + ".json", OutputType.JSON);
					System.out.println(destination + "\\" + fc.getTableName() + ".JSON written to disk.");
					continue;
				} catch (IOException e) {
					System.out.println(IO_ERROR);
					System.exit(0);
				}
									
			} else if (userResponse == 2) {
				System.out.println(DESTINATION_INFO);
				String destination = scan.next();
				try {
					fc.outputData(destination, fc.getTableName() + ".xml", OutputType.XML);
					fc.outputData(destination, fc.getTableName() + ".xsd", OutputType.XML_SCHEMA);
					System.out.println(destination + "\"" + fc.getTableName() + ".XML written to disk.");
					System.out.println(destination + "\"" + fc.getTableName() + ".XSD written to disk.");
					continue;
				} catch (IOException e) {
					System.out.println(IO_ERROR);
					System.exit(0);
				}
			} else if (userResponse == 3) {
				System.out.println(DBMS_INFO);
				
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
						System.out.println(INVALID_INPUT);
						continue;
					}
				}
			} else if (userResponse == 4) {
				System.exit(0);
			} else {
				System.out.println(INVALID_INPUT);
				continue;
			}
		}
	}
	
	// Method to query the user about the database info.
	// Pushes the data to the database.
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
	
	// Method to write the SQL file based on SQLType (MSSQL, MySQL, Postgre)
	private void writeDatabaseFile(SQLType type) {
		System.out.println(DESTINATION_INFO);
		String destination = scan.next();
		
		try {
			fc.outputToSQLFile(new File(destination, fc.getTableName() + ".sql"), fc.getTableName(), type, true, -1);
			System.out.println(destination + "\\" + fc.getTableName() + ".SQL written to disk.");
		} catch (IOException e) {
			System.out.println(IO_ERROR);
			System.exit(0);
		}
	}
	
	// Parses user input either by true/false or yes/no
 	private int parseInput(String response) {
		if (response.equalsIgnoreCase("true") || (response.equalsIgnoreCase("yes"))) {
			return 1;
		} else if (response.equalsIgnoreCase("false") || (response.equalsIgnoreCase("no"))) {
			return 0;
		} else
			return -1;	
	}
	
 	// Method to set the data types for classes.
 	// This requires the fully qualified class name.
	private void setColumnClass(int index, String type) throws ClassNotFoundException {
			Class<?> clazz = Class.forName(type);
			columnClasses[index] = type;
			fc.setColumnClasses(clazz, index);	
	}

	// Queries the FileController for the column classes in the data.
	// Stores them in an array of strings.
	private void createColumnClasses() {
		columnClasses = new String[fc.getFields()];
		for (int i = 0; i < fc.getFields(); i++) {
			columnClasses[i] = fc.getColumnClasses()[i].getSimpleName();
		}
	}
	
	// Method to print the current column classes to the user.
	private void printColumnsClasses() {
		for (int i = 0; i < fc.getFields(); i++) {
				System.out.print("(" + i + ")" + fc.getColumnHeader(i) + ":"  + columnClasses[i] + " ");
		}
		System.out.println();
	}
	
	/**
	 * Assures the file path passed in does indeed exist.
	 * 
	 * @param String filePath - The filepath of the csv file.
	 * <br>
	 * <b>USAGE:</b></br>
	 * <pre>
	 * FileController fc = new FileController;
	 * Cli c = new Cli();
	 * 
	 * c.checkCsv(filePath)
	 * </pre>
	 * @see FileController
	 * @see File
	 */
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

