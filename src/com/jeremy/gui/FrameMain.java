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
	
	private FileController fileCon;
	
	public FrameMain() {
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
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importCSV();
			}
		});
		mnuFile.add(mntmOpen);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnuFile.add(mntmSave);
		
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
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editTable();
			}
		});
		pnlButtons.add(btnEdit);
		
		JButton btnConvert = new JButton("Convert");
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
		
		JLabel lblName = new JLabel("Table Name:");
		pnlTableDetails.add(lblName);
		
		lblNameText = new JLabel("");
		pnlTableDetails.add(lblNameText);
	}
	
	// Opens the import wizard for importing a csv file. Attempts to input data into a table when complete.
	public void importCSV(){
		DialogWizard dw = new DialogWizard();
		this.fileCon = dw.getFileController();
		
		if(fileCon != null){
			tblData.setModel(new TableModelData(fileCon));
			lblNameText.setText(fileCon.getTableName());
			this.invalidate();
		}
	}
	
	// opens the conversion dialog box. provides it the current FileController object
	public void convertTable(){
		DialogConvert dc = new DialogConvert(fileCon);
	}
	
	public void editTable(){
		DialogEdit de = new DialogEdit(fileCon);
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
