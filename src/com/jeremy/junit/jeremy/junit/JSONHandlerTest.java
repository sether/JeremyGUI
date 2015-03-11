package jeremy.junit;

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
		String expected = "{\"numRows\":\"3\",\"numColumns\":\"5\",\"created\":\"11/03/15 11:21 PM\",\"fileName\":\"LasData.csv\",\"user\":\"\",\"columnInfo\": [{\"Column 0\":\"Integer\",\"Column 1\":\"String\",\"Column 2\":\"Float\",\"Column 3\":\"Long\",\"Column 4\":\"Boolean\"}],\"data\": [{\"int\":\"1\",\"string\":\"one\",\"float\":\"0.01\",\"long\":\"1\",\"boolean\":\"true\"},{\"int\":\"2\",\"string\":\"two\",\"float\":\"0.02\",\"long\":\"2\",\"boolean\":\"false\"},{\"int\":\"3\",\"string\":\"three\",\"float\":\"0.03\",\"long\":\"3\",\"boolean\":\"true\"}]}";
		assertEquals("Same?", expected, actual);
	}

}
