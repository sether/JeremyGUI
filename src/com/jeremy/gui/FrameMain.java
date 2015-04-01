package com.jeremy.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JTable;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

import com.jeremy.FileController;
import com.jeremy.Logging;

/**
 * The main window for the Jeremy GUI. Displays csv data in a table allowing modifications and exporting to multiple file types.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class FrameMain extends JFrame {

	private JPanel contentPane;
	private JTable tblData;
	private JLabel lblNameText;
	private JButton btnConvert, btnEdit;
	
	private FileController fileCon;
	
	public FrameMain() {
		setTitle("Jeremy CSV Converter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 683, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// setup top bar menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open CSV...");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importCSV();
			}
		});
		mnuFile.add(mntmOpen);
		
		JMenu mnNewMenu = new JMenu("Table Data");
		mnuFile.add(mnNewMenu);
		
		JMenuItem mntmImport = new JMenuItem("Import...");
		mnNewMenu.add(mntmImport);
		
		JMenuItem mntmExport = new JMenuItem("Export...");
		mnNewMenu.add(mntmExport);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnuFile.add(mntmExit);
		
		JMenu mnuHelp = new JMenu("Help");
		menuBar.add(mnuHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnuHelp.add(mntmAbout);
		
		// panel for right aligned lower buttons
		JPanel pnlButtons = new JPanel();
		contentPane.add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		btnEdit = new JButton("Edit");
		btnEdit.setEnabled(false);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editTable();
			}
		});
		pnlButtons.add(btnEdit);
		
		btnConvert = new JButton("Convert");
		btnConvert.setEnabled(false);
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convertTable();
			}
		});
		pnlButtons.add(btnConvert);
		
		//setup table and scrollpane
		tblData = new JTable();
		JScrollPane scrollPane = new JScrollPane(tblData);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel pnlTableDetails = new JPanel();
		contentPane.add(pnlTableDetails, BorderLayout.NORTH);
		pnlTableDetails.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblNameText = new JLabel("");
		pnlTableDetails.add(lblNameText);
	}
	
	// Opens the import wizard for importing a csv file. Attempts to input data into a table when complete.
	public void importCSV(){
		DialogWizard dw = new DialogWizard();
		this.fileCon = dw.getFileController();
		updateTable();
	}
	
	// opens the conversion dialog box. provides it the current FileController object
	public void convertTable(){
		DialogConvert dc = new DialogConvert(fileCon);
	}
	
	public void editTable(){
		DialogEdit de = new DialogEdit(fileCon);
		updateTable();
	}
	
	public void updateTable(){
		if(fileCon != null){
			tblData.setModel(new TableModelData(fileCon));
			lblNameText.setText("Table Name: " + fileCon.getTableName());
			this.invalidate();
			
			//enable buttons
			btnEdit.setEnabled(true);
			btnConvert.setEnabled(true);
		}
	}
	
	public static void main(String[] args) {
		try {
			FrameMain frame = new FrameMain();
			frame.setVisible(true);
		} catch (Exception e) {
			Logging.getInstance().log(Level.SEVERE, "uncaught error in main", e);
		}
	}

}
