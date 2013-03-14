package uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions;

import com.google.gson.JsonSyntaxException;

public class InvalidMessageException extends Exception {

	public InvalidMessageException(String string) {
		super(string);
	}

	public InvalidMessageException(String string, JsonSyntaxException e) {
		super(string, e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2157565083776781818L;

}
