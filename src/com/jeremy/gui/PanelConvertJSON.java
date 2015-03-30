package com.jeremy.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jeremy.FileController;

/**
 * A JPanel for displaying options regarding JSON conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertJSON extends JPanel{
	private FileController fileCon;

	public PanelConvertJSON(FileController fileCon) {
		this.fileCon = fileCon;
		JLabel lblJSONTest = new JLabel("JSON TEST");
		add(lblJSONTest);
	}
	
	@Override
	public String toString(){
		return "JSON";
	}

}
