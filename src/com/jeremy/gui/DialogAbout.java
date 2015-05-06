package com.jeremy.gui;

import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JTextPane;

import com.jeremy.gui.wrapper.JeremyResourceBundle;

public class DialogAbout extends JDialog{
	public DialogAbout(JeremyResourceBundle rs) {
		this.setModal(true);
		this.setSize(300, 200);
		this.setResizable(false);
		
		//set window location
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.setLocation(width/2 - this.getWidth()/2, height/2 - this.getHeight()/2);
		
		JLabel lblTitle = new JLabel(rs.getString("title"));
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 15));
		getContentPane().add(lblTitle, BorderLayout.NORTH);
		
		JTextPane txtDescription = new JTextPane();
		txtDescription.setEditable(false);
		getContentPane().add(txtDescription, BorderLayout.CENTER);
		
		//add content to desc area
		String s = "";
		s += rs.getString("msgVersion") + ": " + FrameMain.VERSION;
		s += "\n" + rs.getString("msgCopyright") + " ZYXBA CORP 2015";
		
		txtDescription.setText(s);
		
		this.setVisible(true);
	}
	
}
