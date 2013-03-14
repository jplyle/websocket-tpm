package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.keys.TPMKey;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;

public class GenerateKey extends Message implements IMessage {

	private KeyUsage usage;
	
	@Override
	public void respond(ServerState context, WebSocket conn) throws ServerInternalException {
		System.out.println("Got a generate key message");
		
		try {
			TPMKey key = KeyFactory.getKeyFactory().createKeyByUsage(usage);	
			KeyReplyMessage wrap = new KeyReplyMessage();
			wrap.setAllFromTpmKey(key);
			context.send(conn, wrap);
		} catch (ClassCastException | ClassNotFoundException
				| InstantiationException | IllegalAccessException
				| TrustedComputingException | InvalidInputException e) {
			e.printStackTrace();
			throw new ServerInternalException("Could not create key", e);
		}
	}
}


