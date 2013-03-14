package uk.ac.ox.cs.johnlyle.tpmwebsocket.message;

import java.io.Serializable;


public class SubjectMessage implements Serializable {

	private static final long serialVersionUID = 1329170421573591550L;
	
	private String subject;
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
