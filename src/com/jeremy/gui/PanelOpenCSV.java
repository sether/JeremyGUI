package com.jeremy.gui;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.jeremy.gui.wrapper.JeremyResourceBundle;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * A JPanel containing a file selection dialog for importing a csv file.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelOpenCSV extends JPanel{
	private JTextField txtFilePath;
	private File file;

	public PanelOpenCSV(JeremyResourceBundle rs){
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel(rs.getString("msgSpecFile"));
		lblNewLabel.setBounds(10, 11, 430, 14);
		add(lblNewLabel);
		
		JLabel lblFileName = new JLabel(rs.getString("msgFileName") + ": ");
		lblFileName.setBounds(20, 36, 84, 14);
		add(lblFileName);
		
		//setup textfield
		txtFilePath = new JTextField();
		txtFilePath.setBounds(114, 33, 157, 20);
		txtFilePath.setEditable(false);
		add(txtFilePath);
		
		//setup browse button
		JButton btnBrowse = new JButton(rs.getString("Browse"));
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		btnBrowse.setBounds(281, 32, 89, 23);
		add(btnBrowse);
	}
	
	// opens a file dialog and when completed sets the file path in a textfield
	public void openFile(){
		JFileChooser ofd = new JFileChooser();
		ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ofd.showOpenDialog(this);
		this.file = ofd.getSelectedFile();
		
		if(file != null && file.exists()){
			txtFilePath.setText(file.getPath());
		}
	}
	
	// returns the selected file
	public File getFile(){
		return this.file;
	}
}
