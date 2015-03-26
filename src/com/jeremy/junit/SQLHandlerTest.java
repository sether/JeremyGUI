package com.jeremy.junit;

import org.junit.Before;
import org.junit.Test;

import com.jeremy.CSVHandler;
import com.jeremy.SQLHandler;
import com.jeremy.TableData;
import com.jeremy.SQLHandler.SQLType;

/**
 * JUnit test class for SQLHandler.class
 * 
 * @author Ryan Kavanagh
 * @version 1.0
 */
public class SQLHandlerTest {
	CSVHandler csv;
	SQLHandler sql;
	
	@Before
		public void beforeTest() {
		try {
			csv = new CSVHandler();
			csv.setFirstLineUsedAsColumnHeader(true);
	
			TableData td = csv.readCSV("TestData/testDataType.csv");
	
			sql = new SQLHandler(td);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDatabaseCreation(){
		try{	
			sql.createDatabase("", "test", SQLType.MYSQL, "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTableCreation(){
		try{
			sql.createTable("", "test", SQLType.MYSQL, "root", "", true, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInsert(){
		try{
			sql.insertDatabase("", "test", SQLType.MYSQL, "root", "", true, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
