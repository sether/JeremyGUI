package com.jeremy.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.FlowLayout;

public class PanelEditGroup extends JPanel{
	private JTextField textField;
	public PanelEditGroup(){
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblColumn = new JLabel("Column");
		add(lblColumn);
		
		JLabel lblName = new JLabel("Name:");
		add(lblName);
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);
		
		JLabel lblType = new JLabel("Type");
		add(lblType);
		
		JComboBox comboBox = new JComboBox();
		add(comboBox);
		
		JButton btnDelete = new JButton("Delete");
		add(btnDelete);
		
	}
}
