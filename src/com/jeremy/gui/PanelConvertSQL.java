package com.jeremy.gui;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jeremy.FileController;
import com.jeremy.Logging;
import com.jeremy.SQLHandler.SQLType;
import com.jeremy.gui.wrapper.JeremyResourceBundle;

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
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.awt.FlowLayout;

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
	private JeremyResourceBundle rs;

	public PanelConvertSQL(FileController fileCon, JeremyResourceBundle jrs) {
		this.rs = jrs;
		this.fileCon = fileCon;
		setLayout(null);
		
		//title
		JLabel lblSqlOutput = new JLabel(rs.getString("lblSQLFile"));
		lblSqlOutput.setBounds(10, 40, 136, 14);
		add(lblSqlOutput);
		
		//sql file
		JLabel lblConvertToSql = new JLabel(rs.getString("lblConvertTo") + " SQL");
		lblConvertToSql.setHorizontalAlignment(SwingConstants.LEFT);
		lblConvertToSql.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConvertToSql.setBounds(10, 11, 311, 14);
		add(lblConvertToSql);
		
		JButton btnSaveSQL = new JButton(rs.getString("Save"));
		btnSaveSQL.setBounds(213, 36, 108, 23);
		btnSaveSQL.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveSQL();
			}
		});
		add(btnSaveSQL);
		
		//database export
		JLabel lblSqlDatabase = new JLabel(rs.getString("lblSQLDatabase") + ":");
		lblSqlDatabase.setBounds(10, 69, 136, 14);
		add(lblSqlDatabase);
		
		JButton btnExport = new JButton(rs.getString("Export"));
		btnExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportSQL();
			}
		});
		btnExport.setBounds(213, 65, 108, 23);
		add(btnExport);
		
		
		//options title
		JLabel lblOptions = new JLabel(rs.getString("lblOptions"));
		lblOptions.setHorizontalAlignment(SwingConstants.LEFT);
		lblOptions.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOptions.setBounds(10, 94, 100, 14);
		add(lblOptions);
		
		//SQL radio button options
		JPanel pnlRadioButtons = new JPanel();
		pnlRadioButtons.setBorder(BorderFactory.createTitledBorder(rs.getString("lblSQLType")));
		FlowLayout radiofl = (FlowLayout) pnlRadioButtons.getLayout();
		radiofl.setAlignment(FlowLayout.LEFT);
		pnlRadioButtons.setBounds(201, 144, 119, 122);
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
		pnlPrimaryKeys.setBorder(BorderFactory.createTitledBorder(rs.getString("lblPrimaryKey")));
		FlowLayout pkfl = (FlowLayout) pnlPrimaryKeys.getLayout();
		pkfl.setAlignment(FlowLayout.LEFT);
		pnlPrimaryKeys.setBounds(10, 144, 196, 105);
		add(pnlPrimaryKeys);
		
		chkIdentity = new JCheckBox(rs.getString("lblAddIdentityColumn"));
		pnlPrimaryKeys.add(chkIdentity);
		
		lblPK = new JLabel(rs.getString("lblPrimaryKeyColumn"));
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
		JLabel lblDatabaseName = new JLabel(rs.getString("lblDatabaseName") + ":");
		lblDatabaseName.setBounds(10, 119, 108, 14);
		add(lblDatabaseName);
		
		txtDBName = new JTextField(rs.getString("msgExampleDatabase"));
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
	
	public SQLType getSQLType(){
		SQLType st;
		if(rdbtnMysql.isSelected()){
			st = SQLType.MYSQL;
		} else if(rdbtnMsSql.isSelected()){
			st = SQLType.SQLSERVER;
		} else{
			st = SQLType.POSTGRESQL;
		}
		return st;
	}
	
	public int getPKColumn(){
		int PKCol;
		
		if(chkIdentity.isSelected()){
			PKCol = -1;
		} else {
			PKCol = (Integer) cbxPKColumn.getSelectedItem();
		}
		return PKCol;
	}
		
	public void saveSQL(){
		File file = setSaveFile();
		
		if(file != null){
			try {
				fileCon.outputToSQLFile(file, txtDBName.getText(), getSQLType(), chkIdentity.isSelected(), getPKColumn());
			} catch (Exception e) {
				Logging.getInstance().log(Level.WARNING, rs.getString("msgSaveErrorM"), e);
				JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), rs.getString("msgSaveError"), rs.getString("msgSaveErrorT"), JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void exportSQL(){
		final DialogDBInfo dbi = new DialogDBInfo(rs);
		
		if(dbi.getSubmit()){
			Thread thread = new Thread(){
				public void run(){
					try {
						fileCon.outputToDatabase(dbi.getHost(), dbi.getPort(), txtDBName.getText(), getSQLType(), dbi.getUsername(), dbi.getPassword(), chkIdentity.isSelected(), getPKColumn());
					} catch (Exception e) {
						Logging.getInstance().log(Level.WARNING, rs.getString("msgExportError"), e);
						JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), rs.getString("msgExportErrorM"), rs.getString("msgExportErrorT"), JOptionPane.WARNING_MESSAGE);
					}
				}
			};
			
			thread.start();
			@SuppressWarnings("unused")
			DialogProgress dp = new DialogProgress(thread, rs.getString("msgExporting"));
		}
	}
	
	@Override
	public String toString(){
		return "SQL";
	}
}
