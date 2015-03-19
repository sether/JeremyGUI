package Drivers;

import com.jeremy.CSVHandler;
import com.jeremy.FileUtility;
import com.jeremy.SQLHandler;
import com.jeremy.TableData;
import com.jeremy.SQLHandler.SQLType;

public class SQLSERVERDriver {
	
		public static void main(String[] args) {
		try {
			CSVHandler csv = new CSVHandler();
			csv.setFirstLineUsedAsColumnHeader(true);
			csv.setDateFormat("yyyy-MM-dd");
			TableData td = csv.readCSV("TestData/testDataType.csv");

			SQLHandler sql = new SQLHandler(td);
			String s = sql.createSQLFile("name", SQLType.SQLSERVER);
			FileUtility.writeFile("TestData/test.sql", s);
			sql.createDatabase("", "test2", SQLType.SQLSERVER, "sa", "test");
			sql.createTable("", "test2", SQLType.SQLSERVER, "sa", "test");
			sql.insertDatabase("", "test2", SQLType.SQLSERVER, "sa", "test");
			System.out.println("Heyo");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//(Useful for SQL Server connection)
// https://msdn.microsoft.com/en-us/library/bb909712(v=vs.90).aspx 
// http://stackoverflow.com/questions/24592717/error-connection-refused-connect-verify-the-connection-properties