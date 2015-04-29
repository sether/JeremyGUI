package com.jeremy.gui;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jeremy.FileController;
import com.jeremy.Logging;
import com.jeremy.FileController.OutputType;
import com.jeremy.SQLHandler.SQLType;

import javax.swing.JButton;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.FlowLayout;

import javax.swing.border.LineBorder;

import java.awt.Color;

/**
 * A JPanel for displaying options regarding SQL conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertSQL extends JPanel{
	private FileController fileCon;
	private JCheckBox chkIdentity;
	private JTextField txtDBName;
	private JRadioButton rdbtnMysql, rdbtnMsSql, rdbtnPostgresql;
	private JComboBox<Integer> cbxPKColumn;
	private JLabel lblPK;

	public PanelConvertSQL(FileController fileCon) {
		this.fileCon = fileCon;
		setLayout(null);
		
		//title
		JLabel lblSqlOutput = new JLabel("SQL File:");
		lblSqlOutput.setBounds(10, 40, 136, 14);
		add(lblSqlOutput);
		
		//sql file
		JLabel lblConvertToSql = new JLabel("Convert to SQL");
		lblConvertToSql.setHorizontalAlignment(SwingConstants.LEFT);
		lblConvertToSql.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConvertToSql.setBounds(10, 11, 100, 14);
		add(lblConvertToSql);
		
		JButton btnSaveSQL = new JButton("Save");
		btnSaveSQL.setBounds(232, 36, 89, 23);
		btnSaveSQL.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveSQL();
			}
		});
		add(btnSaveSQL);
		
		//database export
		JLabel lblSqlDatabase = new JLabel("SQL Database:");
		lblSqlDatabase.setBounds(10, 69, 136, 14);
		add(lblSqlDatabase);
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(232, 65, 89, 23);
		add(btnExport);
		
		
		//options title
		JLabel lblOptions = new JLabel("Options");
		lblOptions.setHorizontalAlignment(SwingConstants.LEFT);
		lblOptions.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOptions.setBounds(10, 94, 100, 14);
		add(lblOptions);
		
		//SQL radio button options
		JPanel pnlRadioButtons = new JPanel();
		pnlRadioButtons.setBorder(BorderFactory.createTitledBorder("SQL Type"));
		FlowLayout radiofl = (FlowLayout) pnlRadioButtons.getLayout();
		radiofl.setAlignment(FlowLayout.LEFT);
		pnlRadioButtons.setBounds(194, 144, 126, 122);
		add(pnlRadioButtons);
		
		ButtonGroup bg = new ButtonGroup();
		
		rdbtnMysql = new JRadioButton("MySQL");
		rdbtnMysql.setSelected(true);
		pnlRadioButtons.add(rdbtnMysql);
		bg.add(rdbtnMysql);
		
		rdbtnMsSql = new JRadioButton("Microsoft SQL");
		pnlRadioButtons.add(rdbtnMsSql);
		bg.add(rdbtnMsSql);
		
		rdbtnPostgresql = new JRadioButton("PostgreSQL");
		pnlRadioButtons.add(rdbtnPostgresql);
		bg.add(rdbtnPostgresql);
		
		
		//create primary key options
		JPanel pnlPrimaryKeys = new JPanel();
		pnlPrimaryKeys.setBorder(BorderFactory.createTitledBorder("Primary Key"));
		FlowLayout pkfl = (FlowLayout) pnlPrimaryKeys.getLayout();
		pkfl.setAlignment(FlowLayout.LEFT);
		pnlPrimaryKeys.setBounds(10, 144, 181, 105);
		add(pnlPrimaryKeys);
		
		chkIdentity = new JCheckBox("Add Identity Column");
		pnlPrimaryKeys.add(chkIdentity);
		
		lblPK = new JLabel("Primary Key Column");
		pnlPrimaryKeys.add(lblPK);
		
		cbxPKColumn = new JComboBox<Integer>();
		pnlPrimaryKeys.add(cbxPKColumn);
		for(int i = 0; i < fileCon.getFields(); i++){
			cbxPKColumn.addItem(i);
		}
		
		//disable column select when identity is
		chkIdentity.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				cbxPKColumn.setEnabled(!chkIdentity.isSelected());
				lblPK.setEnabled(!chkIdentity.isSelected());
			}
		});
		
		//database name option
		JLabel lblDatabaseName = new JLabel("Database Name: ");
		lblDatabaseName.setBounds(10, 119, 108, 14);
		add(lblDatabaseName);
		
		txtDBName = new JTextField("EXAMPLE_DATABASE");
		txtDBName.setBounds(117, 116, 203, 20);
		add(txtDBName);
		txtDBName.setColumns(10);
	}
	
	//open file save dialog and return selected file
	public File setSaveFile(){
		JFileChooser ofd = new JFileChooser();
		ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ofd.showSaveDialog(this);
		return ofd.getSelectedFile();
	}
		
	public void saveSQL(){
		File file = setSaveFile();
		SQLType st;
		int PKCol;
		
		if(chkIdentity.isSelected()){
			PKCol = -1;
		} else {
			PKCol = (Integer) cbxPKColumn.getSelectedItem();
		}
		
		if(rdbtnMysql.isSelected()){
			st = SQLType.MYSQL;
		} else if(rdbtnMsSql.isSelected()){
			st = SQLType.SQLSERVER;
		} else{
			st = SQLType.POSTGRESQL;
		}
		
		if(file != null){
			try {
				fileCon.outputToSQLFile(file, txtDBName.getText(), st, chkIdentity.isSelected(), PKCol);
			} catch (Exception e) {
				Logging.getInstance().log(Level.WARNING, "Unable to save file", e);
				JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), "Error saving file. See log for details.", "Save Error", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	@Override
	public String toString(){
		return "SQL";
	}
}
