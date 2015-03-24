package com.jeremy.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.jeremy.FileController;
import com.jeremy.FileController.OutputType;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A JPanel for displaying options regarding XML conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertXML extends JPanel implements ConvertI{
	private JTextField txtXMLPath, txtXSDPath;
	private JButton btnBrowseXML, btnBrowseXSD;
	private JCheckBox cbxOutputSchema;
	
	private FileController fileCon;
	private File xsdFile;
	private File xmlFile;

	public PanelConvertXML(FileController fileCon) {
		this.fileCon = fileCon;
		setLayout(null);
		
		//schema option textbox
		cbxOutputSchema = new JCheckBox("Output schema");
		cbxOutputSchema.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbxOutputSchema.isSelected()){
					btnBrowseXSD.setEnabled(true);
				} else {
					btnBrowseXSD.setEnabled(false);
				}
				
			}
		});
		cbxOutputSchema.setSelected(true);
		cbxOutputSchema.setBounds(10, 57, 312, 23);
		add(cbxOutputSchema);
		
		//xml label
		JLabel lblXML = new JLabel("XML Output:");
		lblXML.setBounds(10, 11, 74, 14);
		add(lblXML);
		
		//xml file path textbox
		txtXMLPath = new JTextField();
		txtXMLPath.setEnabled(false);
		txtXMLPath.setBounds(94, 8, 197, 20);
		add(txtXMLPath);
		txtXMLPath.setColumns(10);
		
		//xml browse button and save file dialog
		btnBrowseXML = new JButton("Browse");
		btnBrowseXML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xmlFile = setSaveFile();
				if(xmlFile != null){
					txtXMLPath.setText(xmlFile.getPath());
				} else {
					txtXMLPath.setText("");
				}
				
			}
		});
		btnBrowseXML.setBounds(301, 7, 89, 23);
		add(btnBrowseXML);
		
		//xsd label
		JLabel lblXSD = new JLabel("XSD Output:");
		lblXSD.setBounds(10, 36, 74, 14);
		add(lblXSD);
		
		//xsd file path
		txtXSDPath = new JTextField();
		txtXSDPath.setEnabled(false);
		txtXSDPath.setColumns(10);
		txtXSDPath.setBounds(94, 33, 197, 20);
		add(txtXSDPath);
		
		//xsd browse button and save file dialog
		btnBrowseXSD = new JButton("Browse");
		btnBrowseXSD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				xsdFile = setSaveFile();
				if(xsdFile != null){
					txtXSDPath.setText(xsdFile.getPath());
				} else {
					txtXSDPath.setText("");
				}
			}
		});
		btnBrowseXSD.setBounds(301, 32, 89, 23);
		add(btnBrowseXSD);
	}
	
	//open file save dialog and return selected file
	public File setSaveFile(){
		JFileChooser ofd = new JFileChooser();
		ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
		ofd.showSaveDialog(this);
		return ofd.getSelectedFile();
	}
	
	@Override
	public String toString(){
		return "XML";
	}

	@Override
	public void convert() throws Exception{
		fileCon.outputData(xmlFile, OutputType.XML);
		fileCon.outputData(xsdFile, OutputType.XML_SCHEMA);
	}

}
