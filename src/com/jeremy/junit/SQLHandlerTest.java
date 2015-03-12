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
		sql.createDatabase("", "test", SQLType.MYSQL, "root", "");
	}

	@Test
	public void testTableCreation(){
		sql.createTable("", "test", SQLType.MYSQL, "root", "");
	}
	
	@Test
	public void testInsert(){
		sql.insertDatabase("", "test", SQLType.MYSQL, "root", "");
	}
}
