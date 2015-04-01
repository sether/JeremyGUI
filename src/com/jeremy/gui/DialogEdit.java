package com.jeremy.gui;

import javax.swing.JDialog;

import com.jeremy.FileController;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class DialogEdit extends JDialog{
	private FileController con;
	
	private JPanel pnlFields;
	private ArrayList<PanelEditGroup> fields = new ArrayList<PanelEditGroup>();
	private JTextField txtTableName;
	
	private final Class<?>[] typeList = {byte.class, char.class, short.class, int.class, long.class, float.class, 
			double.class, boolean.class, String.class};
	
	public DialogEdit(FileController con){
		this.con = con;
		
		//setup dialog
		this.setSize(450, 300);
		this.setModal(true);
		this.setResizable(false);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		//edit panel - to contain the PanelEditGroup objects
		pnlFields = new JPanel();
		GridLayout gl_pnlFields = new GridLayout(0, 1);
		pnlFields.setLayout(gl_pnlFields);
		
		//container panel - contains the edit panel and an empty panel to keep the edit panel compressed
		JPanel cont = new JPanel();
		JPanel space = new JPanel();
		cont.setLayout(new BorderLayout());
		cont.add(space, BorderLayout.CENTER);
		cont.add(pnlFields, BorderLayout.NORTH);
		
		JPanel pnlTableName = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pnlTableName.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pnlFields.add(pnlTableName);
		
		JLabel lblTableName = new JLabel("Table Name:");
		pnlTableName.add(lblTableName);
		
		txtTableName = new JTextField(con.getTableName());
		pnlTableName.add(txtTableName);
		txtTableName.setColumns(10);
		
		//button panel
		JPanel buttonPanel = new JPanel();
		FlowLayout fl_buttonPanel = new FlowLayout();
		fl_buttonPanel.setAlignment(FlowLayout.RIGHT);
		buttonPanel.setLayout(fl_buttonPanel);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		buttonPanel.add(btnCancel);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		buttonPanel.add(btnUpdate);
		
		
		//add edit panels
		for(int i = 0; i < con.getFields(); i++){
			PanelEditGroup pe = new PanelEditGroup(i, con.getColumnHeader(i), con.getColumnClasses(i), typeList);
			fields.add(pe);
			pnlFields.add(pe);
		}
		
		JScrollPane scrollPane = new JScrollPane(cont);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void closeDialog(){
		this.dispose();
	}
	
	public void update(){
		//update table name
		con.setTableName(txtTableName.getText());
		
		//iterate through edit panel and update data
		for(PanelEditGroup pe : fields){
			con.getColumnHeader()[pe.getColIndex()] = pe.getColName();
			con.getColumnClasses()[pe.getColIndex()] = typeList[pe.getComboIndex()];
		}
		
		//iterate through edit panel and delete if delete checked
		for(PanelEditGroup pe : fields){
			if(pe.getDelete()){
				//TODO delete
			}
		}
		
		closeDialog();
	}
}
