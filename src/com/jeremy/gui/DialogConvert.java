package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import com.jeremy.FileController;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;

/**
 * A dialog box for selecting a conversion type and outputting the results.
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class DialogConvert extends JDialog {
	private final JPanel contentPanel = new JPanel();
	JComboBox<JPanel> cbxOutputType;
	private CardLayout cl;
	private JPanel cardPanel;
	
	private JPanel xmlPanel;
	private JPanel jsonPanel;
	private JPanel sqlPanel;

	public DialogConvert(FileController fileCon) {
		this.setModal(true);
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setBounds(100, 100, 333, 370);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblOutputType = new JLabel("Output Type:");
		topPanel.add(lblOutputType);
		
		// conversion type combobox
		cbxOutputType = new JComboBox<JPanel>();
		cbxOutputType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == cbxOutputType){
					changeConversion((JPanel) cbxOutputType.getSelectedItem());
				}
			}
		});
		topPanel.add(cbxOutputType);
		
		//add conversion panels to a card panel
		cardPanel = new JPanel();
		cardPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		cl = new CardLayout(0, 0);
		cardPanel.setLayout(cl);
		
		xmlPanel = new PanelConvertXML(fileCon);
		xmlPanel.setBorder(null);
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
	public void changeConversion(JPanel con){
		cl.show(cardPanel, con.toString());
	}
}
