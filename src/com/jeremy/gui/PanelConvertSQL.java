package com.jeremy.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jeremy.FileController;

/**
 * A JPanel for displaying options regarding SQL conversion.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class PanelConvertSQL extends JPanel implements ConvertI{
	private FileController fileCon;

	public PanelConvertSQL(FileController fileCon) {
		this.fileCon = fileCon;
		JLabel lblXmlTest = new JLabel("SQL TEST");
		add(lblXmlTest);
	}
	
	@Override
	public String toString(){
		return "SQL";
	}

	@Override
	public void convert() throws Exception{
		// TODO Auto-generated method stub
		
	}

}
