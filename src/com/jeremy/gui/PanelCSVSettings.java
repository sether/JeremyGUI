package com.jeremy.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import com.jeremy.gui.wrapper.JeremyResourceBundle;

import java.awt.Font;

/**
 * A JPanel for displaying options used when importing a CSV file.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelCSVSettings extends JPanel {
	private JTextField txtTable;
	JCheckBox chkFirstAsCol;
	private JTextField txtDelimiter;
	private JTextField txtDateFormat;

	public PanelCSVSettings(JeremyResourceBundle rs) {
		setLayout(null);
		
		JLabel lblSelectImportSettings = new JLabel(rs.getString("msgAdditionalSettings") + ": ");
		lblSelectImportSettings.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectImportSettings.setBounds(10, 11, 176, 14);
		add(lblSelectImportSettings);
		
		JLabel lblTableName = new JLabel(rs.getString("lblTableName") + ":");
		lblTableName.setBounds(10, 39, 94, 14);
		add(lblTableName);
		
		txtTable = new JTextField();
		txtTable.setText(rs.getString("Table"));
		txtTable.setBounds(104, 36, 84, 20);
		add(txtTable);
		txtTable.setColumns(10);
		
		chkFirstAsCol = new JCheckBox(rs.getString("msgColumNames"));
		chkFirstAsCol.setBounds(206, 35, 217, 23);
		add(chkFirstAsCol);
		
		JLabel lblDelimiter = new JLabel(rs.getString("msgDelimiter") + ":");
		lblDelimiter.setBounds(10, 67, 94, 14);
		add(lblDelimiter);
		
		txtDelimiter = new JTextField();
		txtDelimiter.setToolTipText("");
		txtDelimiter.setText(",");
		txtDelimiter.setColumns(10);
		txtDelimiter.setBounds(104, 64, 84, 20);
		add(txtDelimiter);
		
		JLabel lblDateFormat = new JLabel(rs.getString("msgDateFormat") + ":");
		lblDateFormat.setBounds(10, 95, 94, 14);
		add(lblDateFormat);
		
		txtDateFormat = new JTextField();
		txtDateFormat.setToolTipText("");
		txtDateFormat.setText(rs.getString("dateFormat"));
		txtDateFormat.setColumns(10);
		txtDateFormat.setBounds(104, 92, 84, 20);
		add(txtDateFormat);
	}
	
	// checks if checkbox is selected. If true the first row of data should be the column headings
	public boolean getFirstAsColumn(){
		return chkFirstAsCol.isSelected();
	}
	
	// returns the entered table name
	public String getTableName(){
		return txtTable.getText();
	}
	
	public String getColumnDelimiter(){
		return txtDelimiter.getText();
	}
	
	public String getDateFormat(){
		return txtDateFormat.getText();
	}
}
