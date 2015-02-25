package com.jeremy;

import com.jeremy.JSONTools.JSONMember;
import com.jeremy.JSONTools.JSONObject;
import com.jeremy.JSONTools.JSONProperty;

/**
 * Used to write a content to a file to the JSON format.
 * @author Anthony Howse
 * @version 1.0
 */

public class JSONHandler {
	private TableData  tblData;
	private JSONObject theJSON;
	
	public JSONHandler(TableData data) {
		tblData = data;		
	}

	public void makeJSON() {
		Object[][] data = tblData.getTableData();
		Object[] headings = data[0];
			
		int line = 1; // skip the headings column
		int rows = tblData.getLines();
		int cols = tblData.getFields();
		
		// Make a JSON object
		// TODO: Do we need an identifier for this?
		this.theJSON = new JSONObject("Placeholder");
		
		for (int i = line; i < rows; i++) {
			// Create a new member.
			JSONMember mem = new JSONMember();
			for (int j = 0; j < cols; j++) {
				// Add the name:value pair to the member.
				mem.addProperty(new JSONProperty(headings[j].toString(), data[i][j].toString()));
			}
			// Add the member to the JSON Object.
			theJSON.addMember(mem);
		}
		
	}
}