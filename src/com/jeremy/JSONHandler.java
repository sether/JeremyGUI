package com.jeremy;

import java.util.ArrayList;
import java.util.Date;

/**
 * Used to write a content to a file to the JSON format.
 * More testing
 * @author Anthony Howse
 * @version 1.0
 */

public class JSONHandler {
	private TableData  tblData;
	private JSONObject topLevelObject;
	
	/**
	 * Default constructor for initializing an XMLHandler. Calls the createXMLObjects method.
	 * @param data The TableData object that will be converted to an XML file
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * CSVHandler csv = new CSVHandler();
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * 
	 * JSONHandler json = new JSONHandler(data);
	 *
	 * FileUtility.writeFile("TestData/test.xml", json.JSONToString());
	 * </pre>
	 */
	public JSONHandler(TableData data) {
		tblData = data;	
		makeJSON();
	}

	public String makeJSON() {
		Object[][] data = tblData.getTableData();
		Object[] headings = tblData.getColumnHeader();	
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		Date created = new Date();
		String[] classes = new String[cols];
				
		topLevelObject = new JSONObject();
		topLevelObject.addProperty("numRows", String.valueOf(rows));
		topLevelObject.addProperty("numColumns", String.valueOf(cols));
		topLevelObject.addProperty("created", created.toString());
		topLevelObject.addProperty("user", "");
		topLevelObject.addProperty("clientNo", "");
		
		JSONObject columnObject = new JSONObject(topLevelObject);
		JSONMember columnMember = new JSONMember("columnInfo");
		for (int i = 0; i < cols; i ++) {
			columnMember.addProperty(new JSONProperty("Column " + i, classes[i] = tblData.getColumnClasses()[i].getSimpleName()));
		}

		columnObject.addMember(columnMember);
		
		// Add the data portion of the JSON Object to the top level JSON Object.
		// Create a new member to store the data from the CSV file.
		JSONObject dataObject = new JSONObject(topLevelObject);
		JSONMember dataMember = new JSONMember("data");
		for (int i = 0; i < rows; i++) {			
			for (int j = 0; j < cols; j++) {
				// Add the name:value pair to the member.
				dataMember.addProperty(new JSONProperty(headings[j].toString(), data[i][j].toString()));
			}
		}
		dataObject.addMember(dataMember);
		return JSONToString();

	}
	
	public String JSONToString() {
		String s = "";
		int i = 0;
		int totalSubObjects = topLevelObject.getTotalObjects();		
		s += JSONHandler.JSONHeader();
		s += jsonObjectStringify(topLevelObject);
		for (JSONObject o:topLevelObject.subObjects) {
			if (i != totalSubObjects) {
				s += jsonMemberStringify(o.getMembers()) + ",";
				i ++;
				// if the last sub object, remove the , character.
				if (i == totalSubObjects) {
					s = s.substring(0, s.length() - 1);
				}
			} 
		}
		s += JSONHandler.JSONFooter();
		
		return s;
	}
	
	private String jsonObjectStringify(JSONObject o) {
		String s = "";
		
		if (o.getProperties() != null) {
			s += jsonPropertyStringify(o.getProperties());
		}
				
		return s;
	}
	
	private String jsonMemberStringify(ArrayList<JSONMember> mems) {
		String s = "";
		
		s += '"' + mems.get(0).name + '"' + ": [";
		
		int j = 0; // Count the grouping of columns per row
		int k = 0; // Count of the name:value pairs traversed.
		s += "{"; 
		for (JSONProperty p:mems.get(0).getProperties()) {
			if (j == tblData.getFields()) {
				s += ",";
				j = 0;
				k++;
				if (j == 0) {
					s+= "{";
				}
			}
			
			//  
			if (j < tblData.getFields() && j > 0) {
				s += ",";
			} else if (j == tblData.getFields() - 1) {
				s += "}";
			}
			
			s += "\"" + p.name + "\":\"" + p.value + "\"";
			
			if (k < tblData.getLines() && j == tblData.getFields() -1 ) {
				s += "}";
			} else if (k <= 1 && j == tblData.getFields() -1) {
				s += "}";
			}
			j++;	
		}
		
		s += "]";
		 
		return s;
				
	}
	
	private String jsonPropertyStringify(ArrayList<JSONProperty> props) {
		String s = "";
		
		for (int i = 0; i < props.size();i++) {
			if (i < props.size() - 1){
				s += "\"" + props.get(i).name + "\":\"" + props.get(i).value + "\",";
			} else {
				s += "\"" + props.get(i).name + "\":\"" + props.get(i).value + "\",";
			}
			
		}
		
		return s;		
	}
	// JSON Header string
	private static String JSONHeader() {
		return "{";
	}
	
	private static String JSONFooter() {
		return "}";
	}

	private class JSONObject {
		private ArrayList<JSONObject> subObjects;
		
		private ArrayList<JSONMember> members;
		private ArrayList<JSONProperty> properties;
		private int objectNum = 0; // ID of the object assigned on creation. This is not used by the top level object.

		public JSONObject(){
			this.subObjects = new ArrayList<JSONObject>();
			this.members = new ArrayList<JSONMember>();
		}
		
		public JSONObject(JSONObject o) {
			o.addObject(this);
			this.members = new ArrayList<JSONMember>();
		}
		
		public void addObject(JSONObject o) {
			this.subObjects.add(o);
			this.objectNum++;
		}
		
		public void addMember(JSONMember member) {
			this.members.add(member);
		}
		
		public ArrayList<JSONMember> getMembers() {
			return members;
		}
		
		public ArrayList<JSONObject> getSubObjects() {
			return subObjects;
		}
		
		public void addProperty(String name, String value) {
			if (this.properties == null) {
				this.properties = new ArrayList<JSONProperty>();
			}		
			this.properties.add(new JSONProperty(name, value));
		}

		public ArrayList<JSONProperty> getProperties() {
			if (properties != null){
				return properties;	
			} else
				return null;		
		}
		
		private int getTotalObjects() {
			return subObjects.size();
		}
		
		private int getObjectID() {
			return this.objectNum;
		}
	}	
	
	private class JSONMember {
		String name;
		ArrayList<JSONProperty> properties;
		
		public JSONMember(String name) {
			this.properties = new ArrayList<JSONProperty>();
			this.name = name;
		}
		
		public void addProperty(JSONProperty prop) {
			properties.add(prop);
		}
		
		public ArrayList<JSONProperty> getProperties() {
			return this.properties;
		}
		
	}
	
	private class JSONProperty {
		private String name;
		private String value;
		
		public JSONProperty(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}	
}