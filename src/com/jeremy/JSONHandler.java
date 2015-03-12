package com.jeremy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Used to convert table data to the JSON format.
 * @author Anthony Howse
 * @version 1.2
 */
public class JSONHandler {
	private TableData  tblData;
	private JSONObject topLevelObject;
	
	/**
	 * Default constructor for initializing an JSONHandler. Calls the makeJSON() method.
	 * @param data The TableData object that will be converted to an JSON file
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
	
	/**
	 * Constructs a JSON top level object out of the CSV file once prepared by the TableData class.
	 * This method is called when creating a JSONHandler object.
	 * 
	 */
	private void makeJSON() {
		Object[][] data = tblData.getTableData();
		Object[] headings = tblData.getColumnHeader();	
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		Date created = new Date();
		SimpleDateFormat sf = new SimpleDateFormat();
		String date = sf.format(created);
		String[] classes = new String[cols];
		
		/* Create the top level JSON Object which contains base properties about the file input to the JSONHandler.
		 * Then adds those properties to itself.
		 */
		topLevelObject = new JSONObject();
		topLevelObject.addProperty("numRows", String.valueOf(rows));
		topLevelObject.addProperty("numColumns", String.valueOf(cols));
		topLevelObject.addProperty("created", date);
		topLevelObject.addProperty("fileName", tblData.getTableName() +".csv");
		topLevelObject.addProperty("user", "");
		topLevelObject.addProperty("clientNo", "");
		
		/* Crates a sub object which contains a member with its name:value pairs.
		* This deals with column information such as class representations of the data input. eg: String, Double, Integer.
		*/
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
	}
	
	/**
	 * Returns a String representation of the CSV data in JSON format with as little whitespace as possible.
	 * 
	 * <br/>
	 * <b>USAGE:</b><br/>
	 * <pre>
	 * CSVHandler csv = new CSVHandler();
	 * TableData data = csv.readCSV("TestData/LasData.csv");
	 * 
	 * JSONHandler json = new JSONHandler(data);
	 *
	 * FileUtility.writeFile("TestData/test.json", json.StringifyJSON());
	 * OR
	 * String s = json.stringifyJSON();
	 * </pre>
	 */
	public String stringifyJSON() {
		String s = "";
		int i = 0;
		int totalSubObjects = topLevelObject.getTotalObjects();	
		
		s += JSONHandler.JSONHeader();
		// Append the base properties. (File information)
		s += stringifyJSONObject(topLevelObject);
		// traverse each object and its list of properties
		for (JSONObject o:topLevelObject.subObjects) {
			if (i != totalSubObjects) {
				s += stringifyJSONMembers(o.getMembers()) + ",";
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
	
	/* Stringify the properties of a JSON Object passed in.
	 * @param JSONObect o The JSON Object you wish extract the properties of.
	 * 
	 */
	private String stringifyJSONObject(JSONObject o) {
		String s = "";
		
		if (o.getProperties() != null) {
			s += stringifyJSONProperties(o.getProperties());
		}
				
		return s;
	}
	
	/* Stringify a list of JSON Members, the returned string is a name:value pair representation of the object. The structure as follows:
	 *  "name": [ {array elements}, {array elements} ]
	 */
	private String stringifyJSONMembers(ArrayList<JSONMember> mems) {
		String s = "";
		
		s += '"' + mems.get(0).name + '"' + ": [";
		
		int j = 0; // Count the grouping of columns per row
		int k = 0; // Count of the name:value pairs traversed.
		s += "{"; 
		for (JSONProperty p:mems.get(0).getProperties()) {
			// format each name:value pair append a "," and group each set of pairs to the amount of columns in the CSV file given.
			// also count the total amount of pairs processed.
			if (j == tblData.getFields()) {
				s += ",";
				j = 0;
				k++;
				// if the first pair turn it into an object denoted by the "{".
				if (j == 0) {
					s+= "{";
				}
			}
			
			//  append commas to each pair.
			if (j < tblData.getFields() && j > 0) {
				s += ",";
				// if we're at the final pair, close the object
			} else if (j == tblData.getFields() - 1) {
				s += "}";
			}
			
			// add the information about the current property.
			s += "\"" + p.name + "\":\"" + p.value + "\"";
			
			// Close off the "data" object
			if (k < tblData.getLines() && j == tblData.getFields() -1) {
				s += "}";
			// Close off the "columns" object
			} else if (k <= 1 && j == tblData.getFields() -1) {
				s += "}";
			}
			j++;	
		}
		
		s += "]";
		 
		return s;
				
	}
	
	/* Returns a stringified version of a list of properties
	 * ready for appending or manipulating if required.
	 */
	private String stringifyJSONProperties(ArrayList<JSONProperty> props) {
		String s = "";
		// iterate through the properties and add them to the string s.
		for (int i = 0; i < props.size();i++) {
			if (i < props.size() - 1){
				s += "\"" + props.get(i).name + "\":\"" + props.get(i).value + "\",";
			} 
		}
		return s;		
	}
	
	// JSON Header string
	private static String JSONHeader() {
		return "{";
	}
	
	// JSON Footer string
	private static String JSONFooter() {
		return "}";
	}

	/* Represents a JSON Object which may be a top level object which contains all other objects with their members and name:value pairs.
	 * @author Anthony Howse
	 *
	 */
	private class JSONObject {
		private ArrayList<JSONObject> subObjects;
		
		private ArrayList<JSONMember> members;
		private ArrayList<JSONProperty> properties;
		private int objectNum = 0; // ID of the object assigned on creation. This is not used by the top level object.

		/*
		 * The default constructor for a JSON Object, the first object created will be a top level object and contain a list of sub objects.
		 * a JSON Object may or may not have base level properties.
		 */
		public JSONObject(){
			this.subObjects = new ArrayList<JSONObject>();
			this.members = new ArrayList<JSONMember>();
		}
		
		/*
		 * The second constructor for a JSON Object, this constructor is used to create sub objects which are then stored in the top level object.
		 * 
		 * @param o The JSON Object which this object wishes to add itself to. (Typically a top level JSON Object)
		 */
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
		
		/* A top level object will contain base level properties such as information on the file and number of rows/columns.
		* For the purposes of this application, objects contained in the top level object do not have base properties.
		*/
		public ArrayList<JSONProperty> getProperties() {
			if (properties != null){
				return properties;	
			} else
				return null;		
		}
		/* Returns the total amount of objects, useful for determining
		 * where to include , characters when parsing objects.
		*/
		private int getTotalObjects() {
			return subObjects.size();
		}
		
		// Used in conjunction with getTotalObjects() to determine "," characters between JSON elements.
		private int getObjectID() {
			return this.objectNum;
		}
	}	
	
	/**  A class representing a JSON Member object. The structure for this particular Member format is as follows:
	 * "name":[P"propertyName1":"propertyValue1","propertyName2":"propertyValue2"},"propertyName1":"propertyValue1","propertyName2":"propertyValue2"}]
	 * etc. Note: In this structure JSON Properties in a Member class are represented as an array containing objects which make up
	 * the name:value pairs.
	 * @author Anthony Howse
	 *
	 */
	private class JSONMember {
		String name;
		ArrayList<JSONProperty> properties;
		
		public JSONMember(String name) {
			this.properties = new ArrayList<JSONProperty>();
			this.name = name;
		}
		
		private void addProperty(JSONProperty prop) {
			this.properties.add(prop);
		}
		
		private ArrayList<JSONProperty> getProperties() {
			return this.properties;
		}	
	}
	
	/** A class representing a JSON Property. A property is a name:value pair.
	 * The structure for properties is as follows: <br/>
	 * "name":"value", <br/>
	 * "name2":"value"
	 * <br/>
	 * Note: This structure assumes all name:value pairs are represented as String objects for consistency.
	 * @author Anthony Howse
	 *
	 */
	private class JSONProperty {
		private String name;
		private String value;
		
		public JSONProperty(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}	
}