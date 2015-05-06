package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import com.jeremy.FileController;
import com.jeremy.gui.wrapper.JeremyResourceBundle;

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
	private PanelConvertSQL sqlPanel;
	private JeremyResourceBundle rs;

	public DialogConvert(FileController fileCon, JeremyResourceBundle jrs) {
		this.rs = jrs;
		this.setModal(true);
		this.setTitle(rs.getString("Convert"));
		setSize(333, 370);
		setResizable(false);
		
		//set window location
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.setLocation(width/2 - this.getWidth()/2, height/2 - this.getHeight()/2);
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().setLayout(new BorderLayout());
		
		JPanel topPanel = new JPanel();
		getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblOutputType = new JLabel(rs.getString("lblOutputType") + ":");
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
		
		xmlPanel = new PanelConvertXML(fileCon, rs);
		jsonPanel = new PanelConvertJSON(fileCon, rs);
		sqlPanel = new PanelConvertSQL(fileCon, rs);
		
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
