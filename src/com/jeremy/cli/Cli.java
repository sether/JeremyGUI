package com.jeremy.cli;

import com.jeremy.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cli {
	private CSVHandler csv = null;
	private FileController fc = null;
	private TableData data = null;
	private Scanner scan;
	private File csvFile = null;
	private String filePath = "";
	private int userResponse = 0;
	
	//private String[] headings = null;
	private String[] columnClasses = null;
	
	public Cli() {
		scan = new Scanner(System.in);
		csv = new CSVHandler();
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
			// error check, allow user to exit loop
			System.out.println("Enter a valid path to your csv file.");
			filePath = scan.next();
		}
	}

	private void csvHandling() {
		while (true) {
			System.out.println("Please pick the relevant setup options to your file:");
			System.out.println("1: First row as headings? (Default = False)");
			System.out.println("2: Does your CSV file use a different delimiter character for columns? (Default = \",\")");
			System.out.println("3: Do you require a specific date format? (Default = \"dd/MM/yyyy\")");
			System.out.println("4: Continue");

			userResponse = scan.nextInt();
			
			if (userResponse == 1) {
				while (true) {	
					System.out.println("Set first row as headings to True? (-1 to exit)");
					userResponse = scan.nextInt();
					
					if (userResponse == 1) {
						csv.setFirstLineUsedAsColumnHeader(true);
						System.out.println("First row set as column headings.");
						break;
					} else if (userResponse == -1){
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}

			} else if (userResponse == 2) {
				while (true) {
					System.out.println("Change delimiter?");
					userResponse = scan.nextInt();
					
					if (userResponse == 1) {
						System.out.println("Enter a new delimter: E.g - \".\", \"|\", etc. (-1 to exit)");
						String newDelimiter = scan.next();
						csv.setColumnDelimiter(newDelimiter);
						break;
					} else if (userResponse == -1){
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}
				
			} else if (userResponse == 3) {
				while (true) {
					System.out.println("Enter a new date format? (E.g - \"yyyy/MM/dd\")");
					userResponse = scan.nextInt();
					if (userResponse == 1) {
						String newDateFormat = scan.next();
						// TODO: Parse date before applying it
						csv.setDateFormat(newDateFormat);
						break;
					} else if (userResponse == -1){
						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}
				
			} else if (userResponse == 4) { // Create table data / catch exceptions.
				try {
					data = csv.readCSV(filePath);
				} catch (IOException e) {
					System.out.println("There was an error reading the disk. The program will now exit.");
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
			System.out.println("2: Check table name.");
			System.out.println("3: Continue");
			
			userResponse = scan.nextInt();
			if (userResponse == 1) {
				while (true) {	
					System.out.println("Current column classes are as follows:");
					printColumnsClasses();
					System.out.println("Is this correct?");
					userResponse = scan.nextInt();				
					if (userResponse == 1) {
						break;
					} else if (userResponse == 2) {
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
			} else if (userResponse ==2) {
				
			} else if (userResponse == 3) {
				break;
			}
		}
	}
	
	private void fileExporting() {
		fc = new FileController();
		
		while (true) {
			System.out.println("Please pick the relevant exporting options for your file:");
			System.out.println("1: Export to JSON format.");
			System.out.println("2: Export to XML. (XML, XML SCHEMA)");
			System.out.println("3: Export to SQL. (Postgre, MSSQL, MySQL)");
			System.out.println("4: Exit Program");
			
			userResponse = scan.nextInt();
			if (userResponse == 1) {
				while (true) {	

					userResponse = scan.nextInt();				
					if (userResponse == 1) {
						System.out.println("First, enter the destination of the file.");
						//error check
						String destination = scan.next();
						
						break;
					} else if (userResponse == 2) {

						break;
					} else {
						System.out.println("Enter a valid option.");
						continue;
					}
				}
			} else if (userResponse ==2) {
				
			} else if (userResponse == 3) {
				
			} else if (userResponse == 4) {
				System.exit(0);
			} else {
				System.out.println("Enter a valid option.");
			}
		}
	}
	
	private void setColumnClass(int index, String dataType) {
		columnClasses[index] = dataType;
	}

	private void createColumnClasses() {
		columnClasses = new String[data.getFields()];
		for (int i = 0; i < data.getFields(); i++) {
			columnClasses[i] = data.getColumnClasses()[i].getSimpleName();
		}
	}
	
	private void printColumnsClasses() {
		for (int i = 0; i < data.getFields(); i++) {
				System.out.println("Index " + i + ": " + columnClasses[i]);
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

