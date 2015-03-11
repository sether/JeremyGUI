package com.jeremy.junit;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jeremy.CSVHandler;

public class CSVHandlerTest {

	private CSVHandler csvHandler;
	private String existingFile;
	private String nonExistingFile;
	private String directory;
	//"TestData/", "LasData.csv"
	
	@Before
	public void beforeTest() {
		csvHandler = new CSVHandler();
		csvHandler.setFirstLineUsedAsColumnHeader(true);
		
		existingFile = "LasData.csv";
		nonExistingFile = "noData.csv";
		directory = "TestData/";
		
		System.out.println("Set up");
	}
	
	@Test (expected = FileNotFoundException.class)
	public void testThrowsError() throws IOException {
		csvHandler.readCSV(directory, nonExistingFile);
	}
	
	@Test
	public void testRuns() throws IOException {
		csvHandler.readCSV(directory, existingFile);
	}
	
}
