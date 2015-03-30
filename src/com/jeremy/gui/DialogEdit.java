package com.jeremy.gui;

import javafx.scene.control.ScrollPane.ScrollBarPolicy;

import javax.swing.JDialog;

import com.jeremy.FileController;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogEdit extends JDialog{
	private FileController con;
	
	private JPanel pnlFields;
	private JTable table;
	private ArrayList<PanelEditGroup> fields = new ArrayList<PanelEditGroup>();
	
	public DialogEdit(FileController con){
		this.con = con;
		
		this.setSize(400, 300);
		
		this.setModal(true);
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
		
		//button panel
		JPanel buttonPanel = new JPanel();
		FlowLayout fl_buttonPanel = new FlowLayout();
		fl_buttonPanel.setAlignment(FlowLayout.RIGHT);
		buttonPanel.setLayout(fl_buttonPanel);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		buttonPanel.add(btnCancel);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeField(0);
			}
		});
		buttonPanel.add(btnUpdate);
		
		for(int i = 0; i < 10; i++){
			PanelEditGroup pg = new PanelEditGroup();
			fields.add(pg);
			pnlFields.add(pg);
		}
		
		JScrollPane scrollPane = new JScrollPane(cont);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	public void removeField(int i){
		pnlFields.remove(fields.get(i));
		fields.remove(fields.get(i));
		
		pnlFields.revalidate();
		this.repaint();
	}
}
