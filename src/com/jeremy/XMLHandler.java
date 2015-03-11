package com.jeremy;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to convert TableData into an XML Document
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class XMLHandler {
	private final Double XML_VERSION = 1.0;
	private final String XML_ENCODING = "UTF-8";
	private boolean fieldAsElement;
	private XMLTag root;
	private TableData data;
	
	/**
	 * Default constructor for initializing an XMLHandler. Calls the createXMLObjects method.
	 * @param data The TableData object that will be converted to an XML file
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * CSVHandler csv = new CSVHandler();
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * 
	 * XMLHandler xml = new XMLHandler(data, true);
	 *
	 * FileUtility.writeFile("TestData/test.xml", xml.getXMLString());
	 * FileUtility.writeFile("TestData/test.xsd", xml.getSchemaString());
	 * </pre>
	 */
	public XMLHandler(TableData data, boolean val){
		this.data = data;
		this.fieldAsElement = val;
		createXMLObjects();
	}
	
	/**
	 * Creates an XML object structure using TableData. Will create row data as elements or attributes depending on
	 * the value of FIELD_AS_ELEMENT.
	 */
	private void createXMLObjects(){
		Object[][] tableData = data.getTableData();
		Object[] headings = data.getColumnHeader();
		int rowCount = tableData.length;
		int colCount = headings.length;
		int startLine = 0;
		
		//create base tag in xml hierarchy
		this.root = new XMLTag("Table");
		this.root.attribs.add(new XMLAttribute("name", data.getTableName()));
		
		//iterate through 2d object array
		for(int i = startLine; i < rowCount; i++){
			//add new tag to root for each row
			XMLTag row = new XMLTag("Row");
			this.root.tags.add(row);
			
			//add each field as an element or as an attribute
			for(int j = 0; j < colCount; j++){
				if(fieldAsElement){
					row.elements.add(new XMLElement(headings[j].toString(), tableData[i][j].toString()));
				} else {
					row.attribs.add(new XMLAttribute(headings[j].toString(), tableData[i][j].toString()));
				}
			}
		}
	}
	
	/**
	 * Constructs an xml document string and returns it. Build this string by creating a header then appending the
	 * xml document data to this.
	 * @return XML document contents as a string
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * XMLHandler xml = new XMLHandler(data);
	 * 
	 * String s = xml.getXMLString()
	 * </pre>
	 */
	public String getXMLString(){
		String doc = "";
		doc += createXMLHeader() + "\n";
		doc += createXMLDTD() + "\n";
		doc += tagToString(root, 0);
		return doc;
	}
	
	/**
	 * Constructs an XSD schema that matches the structure of the XML string to be generated. Will output a slightly
	 * different structure as required by using rows as elements or attributes.
	 * @return XSD document matching the XML document
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * XMLHandler xml = new XMLHandler(data);
	 * 
	 * String s = xml.getSchemaString()
	 * </pre>
	 */
	public String getSchemaString(){
		String dataStruc;
		XMLTag container;
		
		//create base of structure
		XMLTag base = new XMLTag("xs:schema");
		base.attribs.add(new XMLAttribute("attributeFormDefault", "unqualified"));
		base.attribs.add(new XMLAttribute("elementFormDefault", "qualified"));
		base.attribs.add(new XMLAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema"));
		
		//non dynamic xml schema content
		XMLTag table = new XMLTag("xs:element");
		base.tags.add(table);
		table.attribs.add(new XMLAttribute("name", "Table"));
		
		XMLTag complex1 = new XMLTag("xs:complexType");
		table.tags.add(complex1);
		
		XMLTag sequence1 = new XMLTag("xs:sequence");
		complex1.tags.add(sequence1);
		
		XMLTag element1 = new XMLTag("xs:element");
		sequence1.tags.add(element1);
		element1.attribs.add(new XMLAttribute("name", "Row"));
		element1.attribs.add(new XMLAttribute("maxOccurs", "unbounded"));
		element1.attribs.add(new XMLAttribute("minOccurs", "0"));
		
		XMLTag complex2 = new XMLTag("xs:complexType");
		element1.tags.add(complex2);
		
		// creates different tag structure from this point depending on if fields should be elements or attributes
		if(fieldAsElement){
			dataStruc = "element";
			
			container = new XMLTag("xs:sequence");
			complex2.tags.add(container);
		} else {
			dataStruc = "attribute";
			
			XMLTag simpleContent = new XMLTag("xs:simpleContent");
			complex2.tags.add(simpleContent);
			
			container = new XMLTag("xs:extension");
			container.attribs.add(new XMLAttribute("base", "xs:string"));
			simpleContent.tags.add(container);
		}
		
		// records field data types as pulled from column classes in TableData object
		for(int i = 0; i < data.getFields(); i++){
			XMLTag fieldElement = new XMLTag("xs:" + dataStruc);
			fieldElement.attribs.add(new XMLAttribute("type", ("xs:" + data.getColumnClasses()[i].getSimpleName().toLowerCase())));
			fieldElement.attribs.add(new XMLAttribute("name", data.getColumnHeader()[i]));
			container.tags.add(fieldElement);
		}
		
		//table name tag
		XMLTag name = new XMLTag("xs:attribute");
		name.attribs.add(new XMLAttribute("type", "xs:string"));
		name.attribs.add(new XMLAttribute("name", "name"));
		complex1.tags.add(name);
		
		
		//generate xml string and return
		return tagToString(base, 0);
	}
	
	/**
	 * Constructs an XML header for usage at the top of an XML document.
	 * @return an XML header
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * XMLHandler xml = new XMLHandler(data);
	 * 
	 * String s = xml.createXMLHeader()
	 * </pre>
	 */
	private String createXMLHeader(){
		return "<?xml version=\"" + XML_VERSION + "\" encoding=\"" + XML_ENCODING + "\"?>";
	}
	
	/**
	 * Constructs a DTD for usage with the XML data
	 * @return an XML DTD
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * XMLHandler xml = new XMLHandler(data);
	 * 
	 * String s = xml.createXMLDTD()
	 * </pre>
	 */
	private String createXMLDTD(){
		String s = "";
		//static portion of DTD definition
		s += "<!DOCTYPE Table [" + "\n";
		s += addTab(1) + "<!ATTLIST Table name CDATA \"\">" + "\n"; // sets table name as attribute
		s += addTab(1) + "<!ELEMENT Table (Row+)>" + "\n";
		
		//dynamic portion of DTD
		String[] st = data.getColumnHeader();
		if(fieldAsElement){ // formatted differently if rows are elements or attributes
			s += addTab(2) + "<!ELEMENT Row (";
			
			//add heading names neatly into row content declaration
			for(int i = 0; i < st.length; i++){
				s += st[i];
				if(i < st.length - 1){ //space and comma separated unless last value
					s += ", ";
				}
			}
			s += ")>" + "\n";
			
			//add element data definitions
			for(int i = 0; i < st.length; i++){
				s += addTab(2) + "<!ELEMENT " + st[i] + " (#PCDATA)>" + "\n";
			}
		} else {
			//add empty row element declaration
			s += addTab(2) + "<!ELEMENT Row EMPTY>" + "\n";
			
			//add attribute data definitions
			for(int i = 0; i < st.length; i++){
				s += addTab(2) + "<!ATTLIST Row " + st[i] + " CDATA \"\">" + "\n";
			}
		}
		
		//end of static DTD
		s += "]>";
			
		return s;
	}
	
	/**
	 * Converts the contents of an XML tag to a string to be used in an XML document. Will create tags and their attributes
	 * and their child objects such as elements and child tags. Operates recursively when dealing with child tags.
	 * @param tag The XMLTag object to generate a string from.
	 * @param indent The initial tab spacing of this tag.
	 * @return A String containing formatted XML data
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * XMLTag tag = new XMLTag("Table");
	 * XMLTag row = new XMLTag("Row");
	 * tag.tags.add(row);
	 * row.elements.add(new XMLElement("id", "1"));
	 * 
	 * String s = xml.tagToString(tag, 0);
	 * </pre>
	 */
	private String tagToString(XMLTag tag, int indent){
		String all = "";
		int ind = indent;
		boolean singleLine = (tag.elements.size() == 0 && tag.tags.size() == 0);
		
		String line = "";
		//format tag start and add attributes
		line = addTab(ind);
		line = "<" + tag.name;
		line += attribToString(tag.attribs);
		
		if(singleLine){ // make single line tag if no child contents exist
			//close start tag with
			line += "/>";
			all += addTab(ind) + line;
		} else {
			//close start tag
			line += ">";
			all += addTab(ind) + line + "\n";
			
			//insert elements
			for(XMLElement elem : tag.elements){
				line = addTab(ind + 1);
				line += "<" + elem.name + attribToString(elem.attribs) + ">";
				line += elem.value;
				line += "</" + elem.name + ">";
				all += line + "\n";
			}
			
			//child tags
			for(XMLTag child : tag.tags){
				all += tagToString(child, indent + 1) + "\n";
			}
			
			//close attribute
			line = "</" + tag.name + ">";
			all += addTab(ind) + line;
		}
		
		return all;
	}
	
	/**
	 * Formats a list of XMLAttribute objects into a neatly spaced string. Spaces multiple values apart. This is used
	 * for neatly adding XML attributes to a tag.
	 * @param a The list of XMLAttribute objects to use
	 * @return a string containing XML style attributes.
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * ArrayList<XMLAttribute> list = new ArrayList<XMLAttribute>();
	 * list.add(new XMLAttribute("id", "01"));
	 * list.add(new XMLAttribute("name", "frank"));
	 * 
	 * String s = xml.attribToString(list);
	 * </pre>
	 */
	private String attribToString(List<XMLAttribute> a){
		String s = "";
		XMLAttribute att;
		if(a.size() > 0){
			s = " ";
			for(int i = 0; i < a.size(); i++){
				att = a.get(i);
				s += att.name + "=" + "\"" + att.value + "\"";
				if(i < a.size() - 1){ // space between attributes if not the last one
					s += " ";
				}
			}
		}
		return s;
	}
	
	/**
	 * Method for creating a number of tab strings to insert into XML documents.
	 * @param indent the number of tabs to generate
	 * @return A string containing the desired number of tabs
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * String text = "this needs 2 tabs before it";
	 * text = addTab(2) + text;
	 * </pre>
	 */
	private String addTab(int indent){
		String s = "";
		for(int i = 0; i < indent; i++){
			s = "\t" + s;
		}
		return s;
	}
	
	/**
	 * A class to represent an XML tag object. XML tags may contain child tags, elements and attributes. A tag must also
	 * contain a name.
	 * @author Scott Micklethwaite
	 * @version 1.0
	 */
	private class XMLTag{
		private List<XMLTag> tags;
		private List<XMLElement> elements;
		private List<XMLAttribute> attribs;
		private String name;
		
		public XMLTag(String name){
			this.name = name;
			this.tags = new ArrayList<XMLTag>();
			this.elements = new ArrayList<XMLElement>();
			this.attribs = new ArrayList<XMLAttribute>();
		}
	}
	
	/**
	 * A class for representing an XML element. An element must have a name and a value. It may additionally contain
	 * a list of attributes.
	 * @author Scott Micklethwaite
	 * @version 1.0
	 */
	private class XMLElement{
		private String name;
		private String value;
		private List<XMLAttribute> attribs;
		
		public XMLElement(String name, String value){
			this.name = name;
			this.value = value;
			this.attribs = new ArrayList<XMLAttribute>();
		}
	}
	
	/**
	 * A class for representing an XML attribute. Must contain a name and a value.
	 * @author Scott Micklethwaite
	 * @version 1.0
	 */
	private class XMLAttribute{
		private String name;
		private String value;
		
		public XMLAttribute(String name, String value){
			this.name = name;
			this.value = value;
		}
	}
}
