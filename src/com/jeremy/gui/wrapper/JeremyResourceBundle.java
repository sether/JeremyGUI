package com.jeremy.gui.wrapper;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.jeremy.Logging;

public class JeremyResourceBundle{
	private ResourceBundle rs;

	public JeremyResourceBundle(ResourceBundle rs){
		this.rs = rs;
	}
	
	//attempt to retrieve translation and log errors when not possible
	public String getString(String s){
		try{
			return rs.getString(s);
		}catch (MissingResourceException ex){
			Logging.getInstance().log(Level.SEVERE, "No translation for: " + s + " in: " + rs.getLocale());
		}
		
		return "TransErr:" + s;
	}
	
	public Locale getLocale(){
		return rs.getLocale();
	}
}
