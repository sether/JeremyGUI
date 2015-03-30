package com.jeremy.gui;

import javax.swing.table.AbstractTableModel;

import com.jeremy.FileController;

/**
 * A table model class for parsing data from a FileController object to a JTable
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class TableModelData extends AbstractTableModel{
	private FileController fc;
	
	public TableModelData(FileController fc){
		this.fc = fc;
	}
	
	@Override
	public int getRowCount() {
		return fc.getLines();
	}

	@Override
	public int getColumnCount() {
		return fc.getFields();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return fc.getTableData()[rowIndex][columnIndex];
	}
	
	@Override
	public String getColumnName(int column){
		return fc.getColumnHeader()[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex){
		// TODO fix issues with converting string to numbers
		//return fc.getColumnClasses()[columnIndex];
		
		return String.class;
	}

}
