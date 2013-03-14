package uk.ac.ox.cs.johnlyle.tpmwebsocket.message;


public class StringMessage extends Message implements ReplyMessage {

	String subject = "Register";
	String message = "";
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
