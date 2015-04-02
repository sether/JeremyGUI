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
			String s = sql.createSQLFile("name", SQLType.POSTGRESQL, true, -1);
			FileUtility.writeFile("TestData/test.sql", s);
			sql.insertDatabase("", "tst5", SQLType.POSTGRESQL, "postgres", "test", true, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}