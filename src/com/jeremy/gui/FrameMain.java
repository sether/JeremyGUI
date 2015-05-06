package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.swing.JScrollPane;
import javax.swing.JLabel;

import com.jeremy.Encrypted;
import com.jeremy.FileController;
import com.jeremy.FileController.OutputType;
import com.jeremy.Logging;
import com.jeremy.TableData;
import com.jeremy.gui.wrapper.JeremyResourceBundle;

import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;

/**
 * The main window for the Jeremy GUI. Displays csv data in a table allowing modifications and exporting to multiple file types.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class FrameMain extends JFrame {
	public static final String VERSION = "1.0";
	
	private JeremyResourceBundle rs;

	private JPanel contentPane;
	private JTable tblData;
	private JLabel lblNameText;
	private JButton btnConvert, btnEdit;
	
	private FileController fileCon;
	
	public FrameMain(JeremyResourceBundle jrs) {
		this.rs = jrs;
		setTitle(rs.getString("title"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 500);
		
		try {
			Image img = ImageIO.read(FrameMain.class.getResource("icon/icon.png"));
			this.setIconImage(img);
		} catch (IOException ex) {
			Logging.getInstance().log(Level.WARNING, "unable to load icon");
		}
		
		//modify UI manager to localise components like openfiledialogs etc
		UIManager.put("FileChooser.openDialogTitleText", rs.getString("ofdOpen"));
		UIManager.put("FileChooser.saveDialogTitleText", rs.getString("ofdSave"));
		UIManager.put("FileChooser.cancelButtonText", rs.getString("ofdCancel"));
		UIManager.put("FileChooser.saveButtonText", rs.getString("ofdSave"));
		UIManager.put("FileChooser.openButtonText", rs.getString("ofdOpen"));
		UIManager.put("FileChooser.filesOfTypeLabelText", rs.getString("ofdFilesOfType") + ":");
		UIManager.put("FileChooser.fileNameLabelText", rs.getString("ofdFileName") + ":");
		SwingUtilities.updateComponentTreeUI(this);
		
		//set window location
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.setLocation(width/2 - this.getWidth()/2, height/2 - this.getHeight()/2);
		
		//setup components
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// setup top bar menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);
		
		JMenuItem mntmOpen = new JMenuItem(rs.getString("mnuOpenCSV"));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importCSV();
			}
		});
		mnuFile.add(mntmOpen);
		
		
		final JMenuItem mntmExport = new JMenuItem(rs.getString("mnuExport"));
		mntmExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exportTableData();
			}
		});
		
		JMenuItem mntmImport = new JMenuItem(rs.getString("mnuImport"));
		mntmImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showImportTableDataDialog();
			}
		});
		
		//code to enable or disable export option based on whether fileCon is null
		JMenu mntmTableData = new JMenu(rs.getString("mnuTableData"));
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
		
		JMenuItem mntmExit = new JMenuItem(rs.getString("mnuExit"));
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		mnuFile.add(mntmExit);
		
		JMenu mnuHelp = new JMenu(rs.getString("mnuHelp"));
		menuBar.add(mnuHelp);
		
		JMenuItem mntmAbout = new JMenuItem(rs.getString("mnuAbout"));
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new DialogAbout(rs);
			}
		});
		mnuHelp.add(mntmAbout);
		
		JMenuItem mntmDoc = new JMenuItem(rs.getString("mnuDocumentation"));
		mntmDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Desktop dt = Desktop.getDesktop();
				try {
					//look for native help
					dt.browse(new File(System.getProperty("user.dir") + "\\docs\\index_" + rs.getLocale() + ".html").toURI());
				} catch (Exception e) {
					try {
						//fallback to english
						dt.browse(new File(System.getProperty("user.dir") + "\\docs\\index_en_US.html").toURI());
					} catch (IOException e1) {
						Logging.getInstance().log(Level.SEVERE, "Missing help file");
					}
				}
			}
		});
		mntmDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mnuHelp.add(mntmDoc);
		
		// panel for right aligned lower buttons
		JPanel pnlButtons = new JPanel();
		contentPane.add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		btnEdit = new JButton(rs.getString("btnEdit"));
		btnEdit.setEnabled(false);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editTable();
			}
		});
		pnlButtons.add(btnEdit);
		
		btnConvert = new JButton(rs.getString("btnConvert"));
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
		DialogWizard dw = new DialogWizard(rs);
		if(dw.getSubmit()){
			this.fileCon = dw.getFileController();
			updateTable();
		}
	}
	
	// opens the conversion dialog box. provides it the current FileController object
	private void convertTable(){
		@SuppressWarnings("unused")
		DialogConvert dc = new DialogConvert(fileCon, rs);
	}
	
	private void editTable(){
		@SuppressWarnings("unused")
		DialogEdit de = new DialogEdit(fileCon, rs);
		updateTable();
	}
	
	private void updateTable(){
		if(fileCon != null){
			tblData.setModel(new TableModelData(fileCon));
			lblNameText.setText(rs.getString("lblTableName") + ": " + fileCon.getTableName());
			this.invalidate();
			
			//enable buttons
			btnEdit.setEnabled(true);
			btnConvert.setEnabled(true);
		}
	}
	
	private void showImportTableDataDialog(){
		File file;
		JFileChooser ofd = new JFileChooser();
		ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ofd.showOpenDialog(this);
		file = ofd.getSelectedFile();
		
		if(file != null && file.exists()){
			importTableData(file);
		}
	}
	
	public void importTableData(File file){
		Object ob = null;
		
		//try to deserialize
		try {
			FileInputStream fs = new FileInputStream(file);
			ObjectInput oi;
			oi = new ObjectInputStream(fs);
			ob = oi.readObject();
			oi.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
				    rs.getString("msgInvalidFile"),
				    rs.getString("msgImportError"),
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		FileController fc = new FileController();
		
		//load data depending on it's file type
		if(ob instanceof Encrypted){ // encrypted file
			//get password
			String password = requestEncryptPassword();
			try {
				if(password != null){
					fc.decryptFile(file, password);
				} else { // return if password not entered. cancelled
					return;
				}
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
					    rs.getString("msgEncryptionPass"),
					    rs.getString("msgImportError"),
					    JOptionPane.WARNING_MESSAGE);
			}
		} else if(ob instanceof TableData){ // non encrypted file
			try {
				fc.readSerialized(file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						rs.getString("msgInvalidFile"),
					    rs.getString("msgImportError"),
					    JOptionPane.WARNING_MESSAGE);
			}
		}
		
		//set new file controller if no errors and refresh
		this.fileCon = fc;
		updateTable();
	}
	
	private void exportTableData(){
		int selectedOption = JOptionPane.showConfirmDialog(null, 
                rs.getString("msgDoYouWantToEncryptThisFile"), 
                rs.getString("msgEncryption"), 
                JOptionPane.YES_NO_OPTION);
		
		try {
			if(selectedOption == 0){ // if yes selected
				//get a password
				String password = createEncryptPassword();
				if(password == null){
					return; //password entry cancelled. don't save.
				}
				
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
			Logging.getInstance().log(Level.WARNING, rs.getString("msgErrorWhileExporting"), e);
			JOptionPane.showMessageDialog(this,
					rs.getString("msgErrorWhileExporting"),
					rs.getString("msgExportError"),
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
		JLabel lblEnter = new JLabel(rs.getString("msgEnterPassword") + ":");
		JPasswordField entPassword = new JPasswordField(10);
		panel.add(lblEnter);
		panel.add(entPassword);
		
		//show dialog
		String[] options = new String[]{rs.getString("OK"), rs.getString("Cancel")};
		int option = JOptionPane.showOptionDialog(null, panel, rs.getString("msgEncryptionPassword"),
		                         JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                         null, options, options[1]);
		
		if(option == 0){// OK button
			password = String.valueOf(entPassword.getPassword());
		}
		
		return password;
	}
	
	private String createEncryptPassword(){
		String password = null;
		
		//create password dialog components
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,2));
		JLabel lblEnter = new JLabel(rs.getString("lblEnterPassword"));
		JPasswordField entPassword = new JPasswordField(10);
		JLabel lblConf = new JLabel(rs.getString("lblConfirmPassword"));
		JPasswordField confPassword = new JPasswordField(10);
		panel.add(lblEnter);
		panel.add(entPassword);
		panel.add(lblConf);
		panel.add(confPassword);
		
		while(password == null){
			//show dialog
			String[] options = new String[]{rs.getString("OK"), rs.getString("Cancel")};
			int option = JOptionPane.showOptionDialog(null, panel, rs.getString("msgEncryptionPassword"),
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
						    rs.getString("msgMismatchPass"),
						    rs.getString("msgPassError"),
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
			//load locale data
			String language;
		    String country;
		    Locale currentLocale;
		    ResourceBundle rs;
	
		    if (args.length == 2) {
		    	language = new String(args[0]);
		        country = new String(args[1]);
		    } else {
		    	country = System.getProperty("user.country"); 
		    	language = System.getProperty("user.language");
		    }
		    
		    /**
	    	System.out.println(language);
	    	System.out.println(country);
	    	**/
		    
		    currentLocale = new Locale(language, country);

			File file = new File("lang/");
			URL[] urls = new URL[]{file.toURI().toURL()};
			ClassLoader loader = new URLClassLoader(urls);
			try{
				rs = ResourceBundle.getBundle("MessagesBundle", currentLocale, loader);
			} catch (MissingResourceException ex){
				//catch non existing local and default to US
				Logging.getInstance().log(Level.WARNING, "Unable to find locale bundle for system. Defaulting to US English.", ex);
				
				//load US bundle
				rs = ResourceBundle.getBundle("MessagesBundle", new Locale("en", "US"), loader);
			}
			
			//wrap the resource bundle
			JeremyResourceBundle jrs = new JeremyResourceBundle(rs);
			
			FrameMain frame = new FrameMain(jrs);
			frame.setVisible(true);
			
			/**
			//Handle args - allow test importing of serialised table data to assist testing.
			if(args.length > 0){
				File file = new File(args[0]);
				if(file.exists()){
					frame.importTableData(file);
				}
			}
			**/
			
		} catch (Exception e) {
			Logging.getInstance().log(Level.SEVERE, "uncaught error in main", e);
		}
	}
}
