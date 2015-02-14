package com.jeremy;

/**
 * Used to store and edit data from a csv file.
 * @author AlexBrown
 * @version 1.0
 */
public class TableData {

	//TODO: Decide how to identify header type
	//private ArrayList<String> headerType;
	private Object[][] tableData;
	private int lines;
	private int fields;
	
	public TableData(Object[][] tableData) {
		this.tableData = tableData;
	}
	
	public TableData(Object[][] tableData, int lines, int fields) {
		this.tableData = tableData;
		this.lines = lines;
		this.fields = fields;
	}

	public Object[][] getTableData() {
		return tableData;
	}

	public void setTableData(Object[][] tableData) {
		this.tableData = tableData;
	}

	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getFields() {
		return fields;
	}

	public void setFields(int fields) {
		this.fields = fields;
	}

	
	
	
}
