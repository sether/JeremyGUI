package com.jeremy.gui;

import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DialogDBInfo extends JDialog{
	private JTextField txtHost;
	private JTextField txtPort;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	private boolean submit;
	
	public DialogDBInfo(){
		setTitle("Database Info");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
		this.setSize(270, 220);
		this.setResizable(false);
		submit = false;
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		//setup form
		JPanel pnlForm = new JPanel();
		getContentPane().add(pnlForm, BorderLayout.NORTH);
		pnlForm.setLayout(new GridLayout(4, 2, 0, 3));
		
		JLabel lblHost = new JLabel("Host");
		pnlForm.add(lblHost);
		
		txtHost = new JTextField();
		pnlForm.add(txtHost);
		txtHost.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		pnlForm.add(lblPort);
		
		txtPort = new JTextField();
		pnlForm.add(txtPort);
		txtPort.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		pnlForm.add(lblUsername);
		
		txtUsername = new JTextField();
		pnlForm.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		pnlForm.add(lblPassword);
		
		txtPassword = new JPasswordField();
		pnlForm.add(txtPassword);
		txtPassword.setColumns(10);
		
		//setup buttons
		JPanel pnlButtons = new JPanel();
		FlowLayout fl_pnlButtons = (FlowLayout) pnlButtons.getLayout();
		fl_pnlButtons.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		pnlButtons.add(btnCancel);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submit = true;
				dispose();
			}
		});
		pnlButtons.add(btnSubmit);
		
		this.setVisible(true);
	}
	
	public boolean getSubmit(){
		return this.submit;
	}
	
	public String getHost(){
		return txtHost.getText();
	}
	
	public String getPort(){
		return txtPort.getText();
	}
	
	public String getPassword(){
		return String.valueOf(txtPassword.getPassword());
	}
	
	public String getUsername(){
		return txtUsername.getText();
	}
}
