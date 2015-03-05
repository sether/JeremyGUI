package com.jeremy;

import java.util.ArrayList;

/**
 * Used to write a content to a file to the JSON format.
 * More testing
 * @author Anthony Howse
 * @version 1.0
 */

public class JSONHandler {
	private TableData  tblData;
	private JSONObject theJSON;
	
	public JSONHandler(TableData data) {
		tblData = data;		
	}

	public String makeJSON() {
		Object[][] data = tblData.getTableData();
		Object[] headings = data[0];
			
		//int line = 1; // skip the headings column
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		
		// Make a JSON object
		// TODO: Do we need an identifier for this?
		this.theJSON = new JSONObject("LasData");
		
		for (int i = 0; i < rows; i++) {
			// Create a new member.
			JSONMember mem = new JSONMember();
			for (int j = 0; j < cols; j++) {
				// Add the name:value pair to the member.
				mem.addProperty(new JSONProperty(headings[j].toString(), data[i][j].toString()));
			}
			// Add the member to the JSON Object.
			theJSON.addMember(mem);
		}
		return JSONToString();
	}
	
	public String JSONToString() {
		String s = "";
		
		s += JSONHandler.JSONHeader();
		s += '"' + theJSON.name + '"' + ":[";
		
		s += processMembers(theJSON.members);
		
		s += JSONHandler.JSONFooter();
		return s;
	}
	
	public String processMembers(ArrayList<JSONMember> members) {
		String s = "";
		int i = 0;
		for (JSONMember m:members){
			s += "{";
			int j = 0;
			for (JSONProperty p: m.properties) {
				if (i != members.size()) {
					
					s+= '"' + p.name + '"' + ":" + p.value;
					
					//handle comma seperation
					if(j < tblData.getFields() - 1){
						s += ",";
					}
					
					//handle end line
					if(j == tblData.getFields() - 1){
						s += "}";
						if(i < tblData.getLines() - 1){
							s += ",";
						}
					}
					
					
					j++;
				}
			}
			i++;
			s+= "\n";
		}
		s += "]}";
		return s;
	}
	
	public static String JSONHeader() {
		return "{";
	}
	
	public static String JSONFooter() {
		return "\n}";
	}
	
	/**
	 * 
	 * @author Anthony Howse
	 *
	 */
	public class JSONObject {
		private String name;
		private ArrayList<JSONMember> members;

		public JSONObject(String name){
			this.name = name;
			this.members = new ArrayList<JSONMember>();
		}

		public ArrayList<JSONMember> getMembers() {
			return members;
		}

		public void setMembers(ArrayList<JSONMember> members) {
			this.members = members;
		}
		
		public void addMember(JSONMember member) {
			members.add(member);
		}
	}	
	
	/**
	 * 
	 * @author Anthony Howse
	 *
	 */
	public class JSONMember {
		ArrayList<JSONProperty> properties;
		
		public JSONMember() {
			this.properties = new ArrayList<JSONProperty>();
		}
		
		public void addProperty(JSONProperty prop) {
			properties.add(prop);
		}
	}
	
	/**
	 * 
	 *
	 * @author A
	 *
	 */
	public class JSONProperty {
		// Maybe a JSON File is more suited to a map structure.
		//private HashMap<String, String> nv;
		private String name;
		private String value;
		
		public JSONProperty(String name, String value) {
			this.name = name;
			this.value = value;
			// Possible future stuff
			//nv = new HashMap<String, String>();
			//nv.put(name, value);
		}
	}
}