package com.jeremy.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.FlowLayout;

import javax.swing.JCheckBox;

import com.jeremy.gui.wrapper.JeremyResourceBundle;

public class PanelEditGroup extends JPanel{
	
	private JTextField txtColumnName;
	private JComboBox<String> cbxType;
	private JCheckBox cbxDelete;
	
	private int colIndex;
	private JeremyResourceBundle rs;
	
	public PanelEditGroup(int columnNum, String columnName, Class<?> dataType, Class<?>[] typeList, JeremyResourceBundle jrs){
		this.rs = jrs;
		colIndex = columnNum;
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
		
		JLabel lblColumn = new JLabel("" + (columnNum + 1));
		add(lblColumn);
		
		JPanel pnlName = new JPanel();
		add(pnlName);
		
		JLabel lblName = new JLabel(rs.getString("msgName") + ":" );
		pnlName.add(lblName);
		
		txtColumnName = new JTextField(columnName);
		pnlName.add(txtColumnName);
		txtColumnName.setColumns(10);
		
		JPanel pnlType = new JPanel();
		add(pnlType);
		
		JLabel lblType = new JLabel(rs.getString("msgType"));
		pnlType.add(lblType);
		
		cbxType = new JComboBox<String>();
		for(int i = 0; i < typeList.length; i++){
			cbxType.addItem(typeList[i].getSimpleName());
			if(dataType.getSimpleName().toLowerCase().equals(typeList[i].getSimpleName().toLowerCase())){
				cbxType.setSelectedIndex(i);
			}
		}
		
		pnlType.add(cbxType);
		
		cbxDelete = new JCheckBox(rs.getString("Delete"));
		add(cbxDelete);
	}
	
	public int getColIndex(){
		return this.colIndex;
	}
	
	public boolean getDelete(){
		return cbxDelete.isSelected();
	}
	
	public int getComboIndex(){
		return cbxType.getSelectedIndex();
	}
	
	public String getColName(){
		return txtColumnName.getText();
	}
}
