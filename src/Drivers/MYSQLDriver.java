package Drivers;

import com.jeremy.CSVHandler;
import com.jeremy.FileUtility;
import com.jeremy.SQLHandler;
import com.jeremy.TableData;
import com.jeremy.SQLHandler.SQLType;

public class MYSQLDriver {
	
		public static void main(String[] args) {
		try {
			CSVHandler csv = new CSVHandler();
			csv.setFirstLineUsedAsColumnHeader(true);
			csv.setDateFormat("yyyy-MM-dd");
			TableData td = csv.readCSV("TestData/testDataType.csv");

			SQLHandler sql = new SQLHandler(td);
			String s = sql.createSQLFile("name", SQLType.MYSQL, true, -1);
			FileUtility.writeFile("TestData/test.sql", s);
		//	sql.createDatabase("", "test2", SQLType.MYSQL, "root", "");
			sql.createTable("", "test00000", SQLType.MYSQL, "root", "", true, -1);
		//	sql.insertDatabase("", "test8000", SQLType.MYSQL, "root", "");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
