package com.jeremy;

/**
 * Used to store and edit data from a csv file.
 * @author AlexBrown
 * @version 1.0
 */
public class TableData {

	private String[] columnHeader;
	private int[] fieldLength;
	private int[] fieldPrecision;
	private Class<?>[] columnClasses;	
	private Object[][] tableData;
	private int lines;
	private int fields;
	
	public TableData(Object[][] tableData, Class<?>[] columnClasses) {
		this.tableData = tableData;
		this.columnClasses = columnClasses;
	}
	
	public TableData(Object[][] tableData, Class<?>[] columnClasses, String[] columnHeader) {
		this.tableData = tableData;
		this.columnClasses = columnClasses;
		this.columnHeader = columnHeader;
	}
	
	public TableData(Object[][] tableData, Class<?>[] columnClasses, int lines, int fields) {
		this.tableData = tableData;
		this.columnClasses = columnClasses;
		this.lines = lines;
		this.fields = fields;
	}
	
	public TableData(Object[][] tableData, Class<?>[] columnClasses, String[] columnHeader, int lines, int fields) {
		this.tableData = tableData;
		this.columnClasses = columnClasses;
		this.columnHeader = columnHeader;
		this.lines = lines;
		this.fields = fields;
	}
	
	public TableData(Object[][] tableData, Class<?>[] columnClasses, String[] columnHeader, int lines, int fields, int[] fieldLength) {
		this.tableData = tableData;
		this.columnClasses = columnClasses;
		this.columnHeader = columnHeader;
		this.lines = lines;
		this.fields = fields;
		this.fieldLength = fieldLength;
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

	public Class[] getColumnClasses() {
		return columnClasses;
	}

	public void setColumnClasses(Class[] columnClasses) {
		this.columnClasses = columnClasses;
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

	public int[] getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(int[] fieldLength) {
		this.fieldLength = fieldLength;
	}

	public int[] getFieldPrecision() {
		return fieldPrecision;
	}

	public void setFieldPrecision(int[] fieldPrecision) {
		this.fieldPrecision = fieldPrecision;
	}

}
