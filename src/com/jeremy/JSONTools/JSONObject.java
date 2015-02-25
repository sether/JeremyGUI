package com.jeremy.JSONTools;

import java.util.ArrayList;
/**
 * Represents a complete JSON Object, which contain members full of property name:value pairs.
 * 
 * @author Anthony Howse
 *
 */
public class JSONObject {
	private String name;
	private ArrayList<JSONMember> members;

	public JSONObject(String name){
		this.name = name;
		this.members = new ArrayList<JSONMember>();
	}

	public ArrayList<JSONMember> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<JSONMember> members) {
		this.members = members;
	}
	
	public void addMember(JSONMember member) {
		members.add(member);
	}
}	