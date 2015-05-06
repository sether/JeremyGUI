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
import com.jeremy.gui.wrapper.JeremyResourceBundle;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingConstants;

import java.awt.Font;

/**
 * A JPanel for displaying options regarding XML conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertXML extends JPanel{
	private JButton btnSaveXML, btnSaveXSD;
	
	private FileController fileCon;
	private JeremyResourceBundle rs;

	public PanelConvertXML(FileController fileCon, JeremyResourceBundle jrs) {
		this.rs = jrs;
		this.fileCon = fileCon;
		setLayout(null);
		
		//xml label
		JLabel lblXML = new JLabel(rs.getString("lblXMLFile") + ":");
		lblXML.setBounds(10, 40, 74, 14);
		add(lblXML);
		
		//xml browse button and save file dialog
		btnSaveXML = new JButton(rs.getString("Save"));
		btnSaveXML.setBounds(210, 36, 111, 23);
		btnSaveXML.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveXML();
			}
		});
		add(btnSaveXML);
		
		//xsd label
		JLabel lblXSD = new JLabel(rs.getString("lblSchema") + ":");
		lblXSD.setBounds(10, 69, 111, 14);
		add(lblXSD);
		
		//xsd browse button and save file dialog
		btnSaveXSD = new JButton(rs.getString("Save"));
		btnSaveXSD.setBounds(210, 65, 111, 23);
		btnSaveXSD.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveXSD();
			}
		});
		add(btnSaveXSD);
		
		JLabel lblConvertToXml = new JLabel(rs.getString("lblConvertTo") + " XML");
		lblConvertToXml.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblConvertToXml.setHorizontalAlignment(SwingConstants.LEFT);
		lblConvertToXml.setBounds(10, 11, 196, 14);
		add(lblConvertToXml);
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
		if(file != null){
			try {
				fileCon.outputData(file, OutputType.XML_SCHEMA);
			} catch (Exception e) {
				Logging.getInstance().log(Level.WARNING, "Unable to save file", e);
				JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), rs.getString("msgSaveError"), rs.getString("msgSaveErrorT"), JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	public void saveXML(){
		File file = setSaveFile();
		if(file != null){
				try {
					fileCon.outputData(file, OutputType.XML);
				} catch (Exception e) {
					Logging.getInstance().log(Level.WARNING, rs.getString("msgSaveErrorM"), e);
					JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), rs.getString("msgSaveError"), rs.getString("msgSaveErrorT"), JOptionPane.WARNING_MESSAGE);
				}
		}
	}
	
	@Override
	public String toString(){
		return "XML";
	}
}
