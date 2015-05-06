package com.jeremy.gui;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jeremy.FileController;
import com.jeremy.Logging;
import com.jeremy.FileController.OutputType;
import com.jeremy.gui.wrapper.JeremyResourceBundle;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.SwingConstants;
import javax.swing.JButton;

/**
 * A JPanel for displaying options regarding JSON conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertJSON extends JPanel{
	private FileController fileCon;
	private JeremyResourceBundle rs;

	public PanelConvertJSON(FileController fileCon,  JeremyResourceBundle jrs) {
		this.rs = jrs;
		this.fileCon = fileCon;
		setLayout(null);
		
		JLabel lblConvertToJson = new JLabel(rs.getString("lblConvertTo") + "JSON");
		lblConvertToJson.setBounds(10, 11, 308, 14);
		lblConvertToJson.setHorizontalAlignment(SwingConstants.LEFT);
		lblConvertToJson.setFont(new Font("Tahoma", Font.BOLD, 11));
		add(lblConvertToJson);
		
		JLabel lblJsonOutput = new JLabel(rs.getString("lblJSONFile") + ":");
		lblJsonOutput.setBounds(10, 40, 136, 14);
		add(lblJsonOutput);
		
		JButton btnJSONSave = new JButton(rs.getString("Save"));
		btnJSONSave.setBounds(215, 36, 106, 23);
		btnJSONSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveJSON();
			}
		});
		add(btnJSONSave);
	}
	
	//open file save dialog and return selected file
		public File setSaveFile(){
			JFileChooser ofd = new JFileChooser();
			ofd.setCurrentDirectory(new File(System.getProperty("user.dir")));
			ofd.showSaveDialog(this);
			return ofd.getSelectedFile();
		}
	
	public void saveJSON(){
		File file = setSaveFile();
		if(file != null){
			try {
				fileCon.outputData(file, OutputType.JSON);
			} catch (Exception e) {
				Logging.getInstance().log(Level.WARNING, "Unable to save file", e);
				JOptionPane.showMessageDialog((Component)getTopLevelAncestor(), rs.getString("msgSaveError"), rs.getString("msgSaveErrorT"), JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	@Override
	public String toString(){
		return "JSON";
	}
}
