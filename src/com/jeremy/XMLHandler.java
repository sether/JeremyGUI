package com.jeremy;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to convert TableData into an XML Document
 * @author Scott Micklethwaite
 * @version 1.0
 * Automagic added edition
 */
public class XMLHandler {
	private final Double XML_VERSION = 1.0;
	private final String XML_ENCODING = "UTF-8";
	private final boolean ROWNUM_AS_ID = false;
	private final boolean FIELD_AS_ELEMENT = false;
	private XMLTag root;
	private TableData data;
	
	public XMLHandler(TableData data){
		this.data = data;
	}
	
	public void createXMLObjects(){
		Object[][] tableData = data.getTableData();
		Object[] headings = data.getColumnHeader();
		int rowCount = tableData.length;
		int colCount = headings.length;
		int startLine = 0;
		
		//create base tag in xml hierarchy
		this.root = new XMLTag("Table");
		
		//iterate through 2d object array
		for(int i = startLine; i < rowCount; i++){
			//add new tag to root for each row
			XMLTag row = new XMLTag("Row");
			this.root.tags.add(row);
			
			//id each row with its row number
			if(ROWNUM_AS_ID){
				row.attribs.add(new XMLAttribute("id", i + ""));
			}
			
			//add each field as an element or as an attribute
			for(int j = 0; j < colCount; j++){
				if(FIELD_AS_ELEMENT){
					row.elements.add(new XMLElement(headings[j].toString(), tableData[i][j].toString()));
				} else {
					row.attribs.add(new XMLAttribute(headings[j].toString(), tableData[i][j].toString()));
				}
			}
		}
	}
	
	//constructs an xml document and returns as a string
	public String getXMLString(){
		String doc = "";
		doc += createXMLHeader() + "\n";
		doc += tagToString(root, 0);
		return doc;
	}
	
	//returns and xml header as a string
	private String createXMLHeader(){
		return "<?xml version=\"" + XML_VERSION + "\" encoding=\"" + XML_ENCODING + "\"?>";
	}
	
	//converts the contents of an XML tag to a string.
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
	
	//creates a string from a list of attributes
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
	
	//method for creating a number of tab strings from an int
	private String addTab(int indent){
		String s = "";
		for(int i = 0; i < indent; i++){
			s = "\t" + s;
		}
		return s;
	}
	
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
	
	private class XMLAttribute{
		private String name;
		private String value;
		
		public XMLAttribute(String name, String value){
			this.name = name;
			this.value = value;
		}
	}
}
