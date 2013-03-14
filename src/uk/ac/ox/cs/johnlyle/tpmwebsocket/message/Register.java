/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket.message;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;

/**
 * @author johl
 *
 */
public class Register extends Message implements IMessage, ReplyMessage {
	
	private String subject = "Register";
	
	public String getSubject() {
		return subject;
	}
	
	@Override
	public void respond(ServerState context, WebSocket conn) {
		context.send(conn, this);
	}
		 
}
