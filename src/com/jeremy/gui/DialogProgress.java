package com.jeremy.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.Timer;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

import java.awt.BorderLayout;
import java.awt.Color;

/***
 * A kickin rad progress bar that blocks input to the rest of an application until a given thread dies.
 * @author Scott Micklethwaite
 *
 */
public class DialogProgress extends JDialog{
	JProgressBar progressBar;
	Thread thread;
	
	
	public DialogProgress(Thread th, String message){
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Busy");
		setModal(true);
		setSize(270,75);
		setResizable(false);
		thread = th;
		
		//setup bar
		progressBar = new JProgressBar();
		getContentPane().add(progressBar, BorderLayout.CENTER);
		progressBar.setStringPainted(true);
		progressBar.setString(message);
	
		progressBar.setUI(new BasicProgressBarUI() {
		      protected Color getSelectionBackground() { return Color.black; }
		      protected Color getSelectionForeground() { return Color.black; }
		});
		
		//perform animation action here
		Timer timer = new Timer(25, new ActionListener(){
			private int i = 0;
			
			private Color b1 = progressBar.getForeground();
			private Color b2 = progressBar.getBackground();

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(thread.isAlive()){
					//flip colors on 100
					if(i >= 100){
						//flip background
						Color tempB = b1;
						b1 = b2;
						b2 = tempB;
						
						progressBar.setForeground(b1);
						progressBar.setBackground(b2);
						
						i = 0;
					}
					
					i++;
					progressBar.setValue(i);
				} else {
					dispose();
				}
				
			}
			
		});
		
		timer.start();
		
		this.setVisible(true);
	}
	
	//Override dispose to stop the dialog from being closed until thread is complete.
	@Override
	public void dispose(){
		if(!thread.isAlive()){ // the user shouldn't ever really get here...
			super.dispose();
		}
	}
}
