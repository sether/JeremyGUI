package com.jeremy.gui;

import java.io.File;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.jeremy.FileController;
import com.jeremy.FileController.OutputType;
import com.jeremy.Logging;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A JPanel for displaying options regarding XML conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertXML extends JPanel{
	private JButton btnSaveXML, btnSaveXSD;
	
	private FileController fileCon;

	public PanelConvertXML(FileController fileCon) {
		this.fileCon = fileCon;
		setLayout(null);
		
		//xml label
		JLabel lblXML = new JLabel("XML Output:");
		lblXML.setBounds(10, 11, 74, 14);
		add(lblXML);
		
		//xml browse button and save file dialog
		btnSaveXML = new JButton("Save");
		btnSaveXML.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveXML();
			}
		});
		btnSaveXML.setBounds(81, 7, 89, 23);
		add(btnSaveXML);
		
		//xsd label
		JLabel lblXSD = new JLabel("XSD Output:");
		lblXSD.setBounds(10, 36, 74, 14);
		add(lblXSD);
		
		//xsd browse button and save file dialog
		btnSaveXSD = new JButton("Save");
		btnSaveXSD.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveXSD();
			}
		});
		btnSaveXSD.setBounds(81, 32, 89, 23);
		add(btnSaveXSD);
	}
	
	//open file save dialog and return selected file
	public File setSaveFile(){
		JFileChooser ofd = new JFileChooser();
		ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ofd.showSaveDialog(this);
		return ofd.getSelectedFile();
	}
	
	public void saveXSD(){
		File file = setSaveFile();
		try {
			fileCon.outputData(file, OutputType.XML_SCHEMA);
		} catch (Exception e) {
			Logging.getInstance().log(Level.WARNING, "Unable to save file", e);
			JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), "Error saving file. See log for details.", "Save Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void saveXML(){
		File file = setSaveFile();
		try {
			fileCon.outputData(file, OutputType.XML);
		} catch (Exception e) {
			Logging.getInstance().log(Level.WARNING, "Unable to save file", e);
			JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), "Error saving file. See log for details.", "Save Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	@Override
	public String toString(){
		return "XML";
	}

}
