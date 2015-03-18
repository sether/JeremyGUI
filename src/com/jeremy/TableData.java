package com.jeremy;

import java.io.Serializable;

/**
 * Used to store and edit data from a csv file.
 * 
 * @author AlexBrown
 * @version 1.0
 */
public class TableData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7537455187991745742L;
	
	private String[] columnHeader;
	private int[] fieldLength;
	private int[] fieldPrecision;
	private Class<?>[] columnClasses;
	private Object[][] tableData;
	private int lines;
	private int fields;
	private String tableName;

	public TableData(Object[][] data, Class<?>[] classes, String name, String[] headers, int lines, int fields) {
		this.tableData = data;
		this.columnClasses = classes;
		this.tableName = name;
		this.columnHeader = headers;
		this.lines = lines;
		this.fields = fields;
	}

	public TableData(Object[][] tableData, Class<?>[] columnClasses, String tableName, String[] columnHeader, int lines, int fields, int[] fieldLength, int[] fieldPrecision) {
		this.tableData = tableData;
		this.columnClasses = columnClasses;
		this.columnHeader = columnHeader;
		this.tableName = tableName;
		this.lines = lines;
		this.fields = fields;
		this.fieldLength = fieldLength;
		this.fieldPrecision = fieldPrecision;
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

	public Class<?>[] getColumnClasses() {
		return columnClasses;
	}

	public void setColumnClasses(Class<?>[] columnClasses) {
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
