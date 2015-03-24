package com.jeremy.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

/**
 * A JPanel for displaying options used when importing a CSV file.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelCSVSettings extends JPanel {
	private JTextField txtTable;
	JCheckBox firstAsColumn;

	public PanelCSVSettings() {
		setLayout(null);
		
		JLabel lblSelectImportSettings = new JLabel("Additional settings:");
		lblSelectImportSettings.setBounds(10, 11, 147, 14);
		add(lblSelectImportSettings);
		
		JLabel lblTableName = new JLabel("Table Name:");
		lblTableName.setBounds(20, 36, 84, 14);
		add(lblTableName);
		
		txtTable = new JTextField();
		txtTable.setText("Table");
		txtTable.setBounds(89, 33, 127, 20);
		add(txtTable);
		txtTable.setColumns(10);
		
		firstAsColumn = new JCheckBox("First row as column names");
		firstAsColumn.setBounds(236, 32, 186, 23);
		add(firstAsColumn);
	}
	
	// checks if checkbox is selected. If true the first row of data should be the column headings
	public boolean getFirstAsColumn(){
		return firstAsColumn.isSelected();
	}
	
	// returns the entered table name
	public String getTableName(){
		return txtTable.getText();
	}
}
