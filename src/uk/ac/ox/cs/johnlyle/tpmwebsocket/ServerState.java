/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket;

import java.security.GeneralSecurityException;

import javax.trustedcomputing.TrustedComputingException;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.ReplyMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.StringMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.KeyFactory;

import com.google.gson.Gson;

/**
 * @author johl
 *
 * This class contains state information.
 *
 */
public class ServerState {

	public void handle(WebSocket conn, IMessage msg) {
		try {
			msg.respond(this, conn);
		} catch (ServerInternalException | InvalidInputException | ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException | TrustedComputingException | GeneralSecurityException e) {
			Gson gson = new Gson();
			conn.send(gson.toJson(e));
		}
	}
	
	public void send(WebSocket conn, ReplyMessage msg) {
		System.out.println("Sending: " + msg.toJsonString());
		conn.send(msg.toJsonString());
	}
	
	public void sendString(WebSocket conn, String s) {
		StringMessage msg = new StringMessage();
		msg.setMessage(s);
		conn.send(msg.toJsonString());
	}

}
