package Drivers;

import com.jeremy.CSVHandler;
import com.jeremy.FileUtility;
import com.jeremy.SQLHandler;
import com.jeremy.TableData;
import com.jeremy.SQLHandler.SQLType;

public class POSTGRESQLDriver {
	
		public static void main(String[] args) {
		try {
			CSVHandler csv = new CSVHandler();
			csv.setFirstLineUsedAsColumnHeader(true);
			csv.setDateFormat("yyyy-MM-dd");
			TableData td = csv.readCSV("TestData/testDataType.csv");

			SQLHandler sql = new SQLHandler(td);
			String s = sql.createSQLFile("name", SQLType.POSTGRESQL);
			FileUtility.writeFile("TestData/test.sql", s);
			sql.createDatabase("", "tst5", SQLType.POSTGRESQL, "postgres", "test");
			sql.createTable("", "tst5", SQLType.POSTGRESQL, "postgres", "test");
			sql.insertDatabase("", "tst5", SQLType.POSTGRESQL, "postgres", "test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}