package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.jeremy.FileController;
import com.jeremy.Logging;
import com.jeremy.gui.wrapper.JeremyResourceBundle;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.awt.CardLayout;
import java.io.IOException;

/**
 * 
 * @author Scott Micklethwaite
 * @version 1.0
 */
public class DialogWizard extends JDialog {
	private boolean submit = false;
	
	private JPanel contentPane, cardPanel;
	private CardLayout cl;
	private JButton btnBack, btnNext, btnFinish;
	
	private PanelOpenCSV pnlOpenCSV;
	private PanelCSVSettings pnlCSVSettings;
	private ArrayList<JPanel> panelList = new ArrayList<JPanel>();
	private int panelIndex = 0;
	
	private FileController fController;
	private JeremyResourceBundle rs;

	public DialogWizard(JeremyResourceBundle rs) {
		this.rs = rs;
		
		//dialog settings
		this.setModal(true);
		this.setTitle(rs.getString("wizTitle"));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(420, 260);
		this.setResizable(false);
		
		//set window location
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		this.setLocation(width/2 - this.getWidth()/2, height/2 - this.getHeight()/2);
		
		//create content
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		//cancel button
		JButton btnCancel = new JButton(rs.getString("Cancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		southPanel.add(btnCancel);
		
		//back button
		btnBack = new JButton(rs.getString("Back"));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backPanel();
			}
		});
		southPanel.add(btnBack);
		
		//next button
		btnNext = new JButton(rs.getString("Next"));
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forwardPanel();
			}
		});
		southPanel.add(btnNext);
		
		//finish button
		btnFinish = new JButton(rs.getString("Finish"));
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFileController();
			}
		});
		btnFinish.setEnabled(false);
		southPanel.add(btnFinish);
		
		//create layout for cards
		cl = new CardLayout(0, 0);
		cardPanel = new JPanel(cl);
		contentPane.add(cardPanel, BorderLayout.CENTER);
		
		//create and index panels
		pnlOpenCSV = new PanelOpenCSV(rs);
		pnlCSVSettings = new PanelCSVSettings(rs);
		this.panelList.add(pnlOpenCSV);
		this.panelList.add(pnlCSVSettings);
		
		
		this.cardPanel.add(panelList.get(0), "0");
		this.cardPanel.add(panelList.get(1), "1");
		
		//set start content and set visible
		this.setPanel(0);
		this.setVisible(true);
	}
	
	//sets the active panel in the cardPanel. Requires the index of panel.
	public void setPanel(int index){
		cl.show(cardPanel, index + "");
		
		btnNext.setEnabled(panelIndex < panelList.size() - 1);
		btnBack.setEnabled(panelIndex > 0);
		btnFinish.setEnabled(panelIndex == panelList.size() - 1);
	}
	
	// go forward a panel
	public void forwardPanel(){
		if(panelIndex < panelList.size()){
			panelIndex++;
			setPanel(panelIndex);
		}
	}
	
	// go back a panel
	public void backPanel(){
		if(panelIndex > 0){
			panelIndex--;
			setPanel(panelIndex);
		}
	}
	
	//creates a FileController object for the main frame to retreive and use.
	public void createFileController(){
		try{
			fController = new FileController();
			
			//pre load settings
			fController.setFirstLineUsedAsColumnHeader(pnlCSVSettings.getFirstAsColumn());
			fController.setColumnDelimiter(pnlCSVSettings.getColumnDelimiter());
			fController.setDateFormat(pnlCSVSettings.getDateFormat());
			
			//load file
			try {
				fController.readFile(pnlOpenCSV.getFile());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, rs.getString("msgUnableOpenFile"));
				Logging.getInstance().log(Level.SEVERE, rs.getString("msgUnableOpenFileT"), e);
			}
			
			//if file name is not to be used as table name
			if (!pnlCSVSettings.getFileNameAsTable()) {
				
				// post load settings
				fController.setTableName(pnlCSVSettings.getTableName());
			}
			
			this.submit = true;
			
			this.dispose();
		} catch (Exception e){
			Logging.getInstance().log(Level.WARNING, rs.getString("msgUnableCreateTableT"), e);
			JOptionPane.showMessageDialog(this, rs.getString("msgUnableCreateTable"), rs.getString("msgImportError"), JOptionPane.WARNING_MESSAGE);
		}
	}
	
	// returns file controller generated with createFileController method.
	public FileController getFileController(){
		return fController;
	}
	
	public boolean getSubmit(){
		return this.submit;
	}
}
