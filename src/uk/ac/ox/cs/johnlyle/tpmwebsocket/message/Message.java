package uk.ac.ox.cs.johnlyle.tpmwebsocket.message;

import com.google.gson.Gson;

public abstract class Message {

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public String toJsonString() {
		return this.toString();
	}
	
	String toJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
}
