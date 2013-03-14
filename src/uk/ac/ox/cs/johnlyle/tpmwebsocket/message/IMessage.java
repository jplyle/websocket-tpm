package uk.ac.ox.cs.johnlyle.tpmwebsocket.message;

import java.security.GeneralSecurityException;

import javax.trustedcomputing.TrustedComputingException;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;

public interface IMessage {
	public void respond(ServerState serverContext, WebSocket conn) throws ServerInternalException, InvalidInputException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException, GeneralSecurityException;
}
