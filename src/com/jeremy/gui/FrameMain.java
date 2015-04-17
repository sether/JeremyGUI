package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

import com.jeremy.Encrypted;
import com.jeremy.FileController;
import com.jeremy.FileController.OutputType;
import com.jeremy.Logging;
import com.jeremy.Serialized;
import com.jeremy.TableData;

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
		
		
		final JMenuItem mntmExport = new JMenuItem("Export...");
		mntmExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportTableData();
			}
		});
		
		JMenuItem mntmImport = new JMenuItem("Import...");
		mntmImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importTableData();
			}
		});
		
		//code to enable or disable export option based on whether fileCon is null
		JMenu mntmTableData = new JMenu("Table Data");
		mntmTableData.addMenuListener(new MenuListener(){

			@Override
			public void menuSelected(MenuEvent e) {
					mntmExport.setEnabled(!(fileCon == null));
			}

			@Override
			public void menuDeselected(MenuEvent e) {/** not needed **/}

			@Override
			public void menuCanceled(MenuEvent e) {/** not needed **/}
			
		});
		mnuFile.add(mntmTableData);
		mntmTableData.add(mntmImport);
		mntmTableData.add(mntmExport);
		
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
	private void importCSV(){
		DialogWizard dw = new DialogWizard();
		this.fileCon = dw.getFileController();
		updateTable();
	}
	
	// opens the conversion dialog box. provides it the current FileController object
	private void convertTable(){
		DialogConvert dc = new DialogConvert(fileCon);
	}
	
	private void editTable(){
		DialogEdit de = new DialogEdit(fileCon);
		updateTable();
	}
	
	private void updateTable(){
		if(fileCon != null){
			tblData.setModel(new TableModelData(fileCon));
			lblNameText.setText("Table Name: " + fileCon.getTableName());
			this.invalidate();
			
			//enable buttons
			btnEdit.setEnabled(true);
			btnConvert.setEnabled(true);
		}
	}
	
	private void importTableData(){
		File file;
		JFileChooser ofd = new JFileChooser();
		ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ofd.showOpenDialog(this);
		file = ofd.getSelectedFile();
		
		if(file != null && file.exists()){
			Object ob = null;
			
			//try to deserialize
			try {
				FileInputStream fs = new FileInputStream(file);
				ObjectInput oi;
				oi = new ObjectInputStream(fs);
				ob = oi.readObject();
				oi.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			FileController fc = new FileController();
			
			//load data depending on it's file type
			if(ob instanceof Encrypted){ // encrypted file
				//get password
				String password = requestEncryptPassword();
				try {
					System.out.println(password);
					fc.decryptFile(file, password);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this,
						    "Incorrect encryption password or invalid file",
						    "Import Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			} else if(ob instanceof TableData){ // non encrypted file
				try {
					fc.readSerialized(file);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this,
						    "Invalid file",
						    "Import Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			}
			
			this.fileCon = fc;
			updateTable();
		}
	}
	
	private void exportTableData(){
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                "Do you want to encrypt this file?", 
                "Encryption", 
                JOptionPane.YES_NO_OPTION);
		
		try {
			if(selectedOption == 0){ // if yes selected
				//get a password
				String password = createEncryptPassword();
				
				//select save location
				File file = exportSaveDialog(".etd");
				
				//use api encryption methods
				if(password != null && file != null){
					fileCon.encryptFile(file, password);
				}
			} else if(selectedOption == 1){ //if no selected
				//select save location
				File file = exportSaveDialog(".std");
				
				//use api serialization methods
				if(file != null){
					fileCon.outputData(file, OutputType.SERIALIZED);
				}
			}
		} catch (Exception e) {
			Logging.getInstance().log(Level.WARNING, "Error while exporting file. May not have saved.", e);
			JOptionPane.showMessageDialog(this,
				    "Error while exporting file. May not have saved. See log for details.",
				    "Export Error",
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private File exportSaveDialog(String extension){
		JFileChooser ofd = new JFileChooser();
		ofd.setSelectedFile(new File(System.getProperty("user.dir") + "/" + fileCon.getTableName().toLowerCase() + extension));
		ofd.showSaveDialog(this);
		return ofd.getSelectedFile();
	}
	
	private String requestEncryptPassword(){
		String password = null;
		
		//create password dialog components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		JLabel lblEnter = new JLabel("Enter password:");
		JPasswordField entPassword = new JPasswordField(10);
		panel.add(lblEnter);
		panel.add(entPassword);
		
		//show dialog
		String[] options = new String[]{"OK", "Cancel"};
		int option = JOptionPane.showOptionDialog(null, panel, "Encryption Password",
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         null, options, options[1]);
		
		if(option == 1){// OK button
			System.out.println("password value set");
			password = String.valueOf(entPassword.getPassword());
		}
		
		return password;
	}
	
	private String createEncryptPassword(){
		String password = null;
		
		//create password dialog components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		JLabel lblEnter = new JLabel("Enter password:");
		JPasswordField entPassword = new JPasswordField(10);
		JLabel lblConf = new JLabel("Confirm password:");
		JPasswordField confPassword = new JPasswordField(10);
		panel.add(lblEnter);
		panel.add(entPassword);
		panel.add(lblConf);
		panel.add(confPassword);
		
		while(password == null){
			//show dialog
			String[] options = new String[]{"OK", "Cancel"};
			int option = JOptionPane.showOptionDialog(null, panel, "Encryption Password",
			                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
			                         null, options, options[1]);
			
			//handle dialog returns
			if(option == 0) {// pressing OK button
				//ensure they match and dont allow emptys
				if(String.valueOf(entPassword.getPassword()).equals(String.valueOf(confPassword.getPassword())) 
						&& entPassword.getPassword().length > 0){ 
					password = String.valueOf(entPassword.getPassword());
				} else {
					JOptionPane.showMessageDialog(this,
						    "Both passwords must match and must not be empty",
						    "Password Error",
						    JOptionPane.WARNING_MESSAGE);
				}
			} else {//option other than OK is chosen
				return null;
			}
		}
		
		return password;
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
