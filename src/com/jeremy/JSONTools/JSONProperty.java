package com.jeremy.JSONTools;

import java.util.HashMap;

public class JSONProperty {
	// Maybe a JSON File is more suited to a map structure.
	//private HashMap<String, String> nv;
	private String name;
	private String value;
	
	public JSONProperty(String name, String value) {
		this.name = name;
		this.value = value;
		// Possible future stuff
		//nv = new HashMap<String, String>();
		//nv.put(name, value);
	}

}
