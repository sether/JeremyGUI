package com.jeremy.JSONTools;

import java.util.ArrayList;


public class JSONMember {
		ArrayList<JSONProperty> properties;
		
		public JSONMember() {
			this.properties = new ArrayList<JSONProperty>();
		}
		
		public void addProperty(JSONProperty prop) {
			properties.add(prop);
		}
}
