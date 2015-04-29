package com.jeremy.gui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import com.jeremy.FileController;
import com.jeremy.Logging;

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
	private final String title = "CSV Import Wizard";
	private boolean submit = false;
	
	private JPanel contentPane, cardPanel;
	private CardLayout cl;
	private JButton btnBack, btnNext, btnFinish;
	
	private PanelOpenCSV pnlOpenCSV;
	private PanelCSVSettings pnlCSVSettings;
	private ArrayList<JPanel> panelList = new ArrayList<JPanel>();
	private int panelIndex = 0;
	
	private FileController fController;

	public DialogWizard() {
		//dialog settings
		this.setModal(true);
		this.setTitle(title);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 420, 260);
		this.setResizable(false);
		
		//create content
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
		southPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		//cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		southPanel.add(btnCancel);
		
		//back button
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				backPanel();
			}
		});
		southPanel.add(btnBack);
		
		//next button
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forwardPanel();
			}
		});
		southPanel.add(btnNext);
		
		//finish button
		btnFinish = new JButton("Finish");
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
		pnlOpenCSV = new PanelOpenCSV();
		pnlCSVSettings = new PanelCSVSettings();
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
				JOptionPane.showMessageDialog(this, "Unable to open file. See error log for details.");
				Logging.getInstance().log(Level.SEVERE, "Unable to open file", e);
			}
			
			// post load settings
			fController.setTableName(pnlCSVSettings.getTableName());
			
			this.submit = true;
			
			this.dispose();
		} catch (Exception e){
			Logging.getInstance().log(Level.WARNING, "Unable to create table", e);
			JOptionPane.showMessageDialog(this, "Unable to create table. Check settings.", "Import Error", JOptionPane.WARNING_MESSAGE);
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
