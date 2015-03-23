package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.jeremy.FileController;
import com.jeremy.Logging;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

/**
 * A dialog box for selecting a conversion type and outputting the results. Uses a state pattern with PanelConvertJSON,
 * PanelConvertXML and PanelConvertSQL
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class DialogConvert extends JDialog {
	private final JPanel contentPanel = new JPanel();
	JComboBox<ConvertI> cbxOutputType;
	private CardLayout cl;
	private JPanel cardPanel;
	
	private ConvertI selected;
	private ConvertI xmlPanel;
	private ConvertI jsonPanel;
	private ConvertI sqlPanel;

	public DialogConvert(FileController fileCon) {
		this.setModal(true);
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setBounds(100, 100, 400, 300);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblOutputType = new JLabel("Output Type:");
		topPanel.add(lblOutputType);
		
		//setup convert button
		JButton btnConvert = new JButton("Convert");
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convert();
			}
		});
		getContentPane().add(btnConvert, BorderLayout.SOUTH);
		
		// conversion type combobox
		cbxOutputType = new JComboBox<ConvertI>();
		cbxOutputType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == cbxOutputType){
					changeConversion((ConvertI) cbxOutputType.getSelectedItem());
				}
			}
		});
		topPanel.add(cbxOutputType);
		
		//add conversion panels to a card panel
		cardPanel = new JPanel();
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		cl = new CardLayout(0, 0);
		cardPanel.setLayout(cl);
		
		xmlPanel = new PanelConvertXML(fileCon);
		jsonPanel = new PanelConvertJSON(fileCon);
		sqlPanel = new PanelConvertSQL(fileCon);
		
		cardPanel.add(xmlPanel.toString(), (Component) xmlPanel);
		cardPanel.add(jsonPanel.toString(), (Component) jsonPanel);
		cardPanel.add(sqlPanel.toString(), (Component) sqlPanel);
		
		//set initial to XML
		changeConversion(xmlPanel);
		
		//add conversion types into drop down menu
		cbxOutputType.addItem(xmlPanel);
		cbxOutputType.addItem(jsonPanel);
		cbxOutputType.addItem(sqlPanel);
		
		this.setVisible(true);
	}
	
	// change state panel
	public void changeConversion(ConvertI con){
		this.selected = con;
		cl.show(cardPanel, con.toString());
	}
	
	// perform convert behavior using selected state
	public void convert(){
		try{
			this.selected.convert();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Unable to save file. See error log for details.");
			Logging.getInstance().log(Level.SEVERE, "Unable to save file", e);
		}
	}

}
