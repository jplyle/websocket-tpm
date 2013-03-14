package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.attest;

import java.security.GeneralSecurityException;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.tools.Attestor;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.KeyFactory;

public class Attest extends Message implements IMessage {

	private String subject = "Attest";
	
	@Override
	public void respond(ServerState state, WebSocket conn)
			throws ServerInternalException, InvalidInputException,
			ClassCastException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, TrustedComputingException,
			GeneralSecurityException {
		
		Attestor attestor = KeyFactory.getKeyFactory().getContext().getAttestor();
		//TODO
		
	}
	
}
