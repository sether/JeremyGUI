package com.jeremy.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jeremy.TableData;
import com.jeremy.XMLHandler;

/**
 * A JUnit test class for the XMLHandler class. Expected strings have been validated to XML standards.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class XMLHandlerTest {
	private TableData tblData;
	
	
	@Before
	public void beforeTest() {
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
	
	/**
	 * Test that fields as element format outputs XML correctly
	 */
	@Test
	public void getXMLStringElement() {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
			expected += "<!DOCTYPE Table [" + "\n";
			expected += "	<!ATTLIST Table name CDATA \"\">" + "\n";
			expected += "	<!ELEMENT Table (Row+)>" + "\n";
			expected += "		<!ELEMENT Row (int, string, float, long, boolean)>" + "\n";
			expected += "		<!ELEMENT int (#PCDATA)>" + "\n";
			expected += "		<!ELEMENT string (#PCDATA)>" + "\n";
			expected += "		<!ELEMENT float (#PCDATA)>" + "\n";
			expected += "		<!ELEMENT long (#PCDATA)>" + "\n";
			expected += "		<!ELEMENT boolean (#PCDATA)>" + "\n";
			expected += "]>" + "\n";
			expected += "<Table name=\"Test Table\">" + "\n";
			expected += "	<Row>" + "\n";
			expected += "		<int>1</int>" + "\n";
			expected += "		<string>one</string>" + "\n";
			expected += "		<float>0.01</float>" + "\n";
			expected += "		<long>1</long>" + "\n";
			expected += "		<boolean>true</boolean>" + "\n";
			expected += "	</Row>" + "\n";
			expected += "	<Row>" + "\n";
			expected += "		<int>2</int>" + "\n";
			expected += "		<string>two</string>" + "\n";
			expected += "		<float>0.02</float>" + "\n";
			expected += "		<long>2</long>" + "\n";
			expected += "		<boolean>false</boolean>" + "\n";
			expected += "	</Row>" + "\n";
			expected += "	<Row>" + "\n";
			expected += "		<int>3</int>" + "\n";
			expected += "		<string>three</string>" + "\n";
			expected += "		<float>0.03</float>" + "\n";
			expected += "		<long>3</long>" + "\n";
			expected += "		<boolean>true</boolean>" + "\n";
			expected += "	</Row>" + "\n";
			expected += "</Table>";
		XMLHandler xml = new XMLHandler(tblData, true);
		
		assertNotNull("XMLHandler is null", xml);
		assertEquals("Actual output does not match expected", expected, xml.getXMLString());
	}
	
	/**
	 * Test that fields as attribute format outputs XML correctly
	 */
	@Test
	public void getXMLStringAttribute() {
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n";
				expected += "<!DOCTYPE Table [" + "\n";
				expected += "	<!ATTLIST Table name CDATA \"\">" + "\n";
				expected += "	<!ELEMENT Table (Row+)>" + "\n";
				expected += "		<!ELEMENT Row EMPTY>" + "\n";
				expected += "		<!ATTLIST Row int CDATA \"\">" + "\n";
				expected += "		<!ATTLIST Row string CDATA \"\">" + "\n";
				expected += "		<!ATTLIST Row float CDATA \"\">" + "\n";
				expected += "		<!ATTLIST Row long CDATA \"\">" + "\n";
				expected += "		<!ATTLIST Row boolean CDATA \"\">" + "\n";
				expected += "]>" + "\n";
				expected += "<Table name=\"Test Table\">" + "\n";
				expected += "	<Row int=\"1\" string=\"one\" float=\"0.01\" long=\"1\" boolean=\"true\"/>" + "\n";
				expected += "	<Row int=\"2\" string=\"two\" float=\"0.02\" long=\"2\" boolean=\"false\"/>" + "\n";
				expected += "	<Row int=\"3\" string=\"three\" float=\"0.03\" long=\"3\" boolean=\"true\"/>" + "\n";
				expected += "</Table>";
		XMLHandler xml = new XMLHandler(tblData, false);
		
		assertNotNull("XMLHandler is null", xml);
		assertEquals("Actual output does not match expected", expected, xml.getXMLString());
	}
	
	/**
	 * Test that fields as elements format outputs a correct schema
	 */
	@Test
	public void getXMLSchemaElement() {
		String expected = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">" + "\n";
		expected += "	<xs:element name=\"Table\">" + "\n";
		expected += "		<xs:complexType>" + "\n";
		expected += "			<xs:sequence>" + "\n";
		expected += "				<xs:element name=\"Row\" maxOccurs=\"unbounded\" minOccurs=\"0\">" + "\n";
		expected += "					<xs:complexType>" + "\n";
		expected += "						<xs:sequence>" + "\n";
		expected += "							<xs:element type=\"xs:integer\" name=\"int\"/>" + "\n";
		expected += "							<xs:element type=\"xs:string\" name=\"string\"/>" + "\n";
		expected += "							<xs:element type=\"xs:float\" name=\"float\"/>" + "\n";
		expected += "							<xs:element type=\"xs:long\" name=\"long\"/>" + "\n";
		expected += "							<xs:element type=\"xs:boolean\" name=\"boolean\"/>" + "\n";
		expected += "						</xs:sequence>" + "\n";
		expected += "					</xs:complexType>" + "\n";
		expected += "				</xs:element>" + "\n";
		expected += "			</xs:sequence>" + "\n";
		expected += "			<xs:attribute type=\"xs:string\" name=\"name\"/>" + "\n";
		expected += "		</xs:complexType>" + "\n";
		expected += "	</xs:element>" + "\n";
		expected += "</xs:schema>";
		XMLHandler xml = new XMLHandler(tblData, true);
		
		assertNotNull("XMLHandler is null", xml);
		assertEquals("Actual output does not match expected", expected, xml.getSchemaString());
	}
	
	/**
	 * Test that fields as attribute format outputs a correct schema
	 */
	@Test
	public void getXMLSchemaAttribute() {
		String expected = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">" + "\n";
		expected += "	<xs:element name=\"Table\">" + "\n";
		expected += "		<xs:complexType>" + "\n";
		expected += "			<xs:sequence>" + "\n";
		expected += "				<xs:element name=\"Row\" maxOccurs=\"unbounded\" minOccurs=\"0\">" + "\n";
		expected += "					<xs:complexType>" + "\n";
		expected += "						<xs:simpleContent>" + "\n";
		expected += "							<xs:extension base=\"xs:string\">" + "\n";
		expected += "								<xs:attribute type=\"xs:integer\" name=\"int\"/>" + "\n";
		expected += "								<xs:attribute type=\"xs:string\" name=\"string\"/>" + "\n";
		expected += "								<xs:attribute type=\"xs:float\" name=\"float\"/>" + "\n";
		expected += "								<xs:attribute type=\"xs:long\" name=\"long\"/>" + "\n";
		expected += "								<xs:attribute type=\"xs:boolean\" name=\"boolean\"/>" + "\n";
		expected += "							</xs:extension>" + "\n";
		expected += "						</xs:simpleContent>" + "\n";
		expected += "					</xs:complexType>" + "\n";
		expected += "				</xs:element>" + "\n";
		expected += "			</xs:sequence>" + "\n";
		expected += "			<xs:attribute type=\"xs:string\" name=\"name\"/>" + "\n";
		expected += "		</xs:complexType>" + "\n";
		expected += "	</xs:element>" + "\n";
		expected += "</xs:schema>";
		XMLHandler xml = new XMLHandler(tblData, false);
		
		assertNotNull("XMLHandler is null", xml);
		assertEquals("Actual output does not match expected", expected, xml.getSchemaString());
	}

}
