package com.jeremy;

/**
 * Used to store and edit data from a csv file.
 * @author AlexBrown
 * @version 1.0
 */
public class TableData {

	//TODO: Decide how to identify header type like date and number
	//private ArrayList<String> headerType;
	private String[] columnHeader;
	private Object[][] tableData;
	private int lines;
	private int fields;
	
	public TableData(Object[][] tableData) {
		this.tableData = tableData;
	}
	
	public TableData(Object[][] tableData, String[] columnHeader) {
		this.tableData = tableData;
		this.columnHeader = columnHeader;
	}
	
	public TableData(Object[][] tableData, int lines, int fields) {
		this.tableData = tableData;
		this.lines = lines;
		this.fields = fields;
	}
	
	public TableData(Object[][] tableData, String[] columnHeader, int lines, int fields) {
		this.tableData = tableData;
		this.columnHeader = columnHeader;
		this.lines = lines;
		this.fields = fields;
	}

	public Object[][] getTableData() {
		return tableData;
	}

	public void setTableData(Object[][] tableData) {
		this.tableData = tableData;
	}

	public String[] getColumnHeader() {
		return columnHeader;
	}

	public void setColumnHeader(String[] columnHeader) {
		this.columnHeader = columnHeader;
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
