package com.jeremy.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jeremy.TableData;
import com.jeremy.JSONHandler;



public class JSONHandlerTest {
	private TableData tblData;
	
	@Before
	public void testBefore() {
		//table data
		Object[][] data = {
				{1, "one", 0.01f, 1L, true},
				{2, "two", 0.02f, 2L, false},
				{3, "three", 0.03f, 3L, true}
				};
		
		//table classes
		Class<?>[] classes = {Integer.class, String.class, Float.class, Long.class, Boolean.class};
		
		//table name
		String name = "Test Table";
		
		//column headers
		String[] headers = {"int", "string", "float", "long", "boolean"};
		
		//lengths
		int lines = data.length;
		int fields = headers.length;
		
		this.tblData = new TableData(data, classes, name, headers, lines, fields);
	}
	
	@Test
	public void testJSONHandler() {
		assertNotNull("tblData is not null", tblData);
	}

	@Test
	public void testMakeJSON() {
		JSONHandler json = new JSONHandler(tblData);
	}

	@Test
	public void testStringifyJSON() {
		JSONHandler json = new JSONHandler(tblData);
		String actual = json.stringifyJSON();
	}

}
