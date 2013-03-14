package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.sign;

import java.security.GeneralSecurityException;
import java.util.UUID;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.keys.SigningKey;
import javax.trustedcomputing.tpm.keys.TPMKey;
import javax.trustedcomputing.tpm.tools.Signer;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.ReplyMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.KeyFactory;

public class Verify extends Message implements IMessage {

	private String subject = "Verify";
	private byte[] signedData;
	private byte[] data;
	private UUID key;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage#respond(uk.ac.ox.cs
	 * .johnlyle.tpmwebsocket.ServerContext, org.java_websocket.WebSocket)
	 */
	@Override
	public void respond(ServerState serverContext, WebSocket conn)
			throws ServerInternalException, InvalidInputException,
			ClassCastException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, TrustedComputingException, GeneralSecurityException {
		
		if (data == null || signedData == null || key == null || key.equals("") || data.length == 0 || signedData.length == 0) {
			throw new InvalidInputException("Missing key, signature or data, all needed for Verify.");
		}
		
		Signer signer = KeyFactory.getKeyFactory().getContext().getSigner();
		
		TPMKey tpmkey = KeyFactory.getKeyFactory().findKeyByUUID(key);
		
		if (tpmkey instanceof SigningKey) {
			boolean result = signer.validate(signedData, data, ((SigningKey) tpmkey).getPublicKey());
			VerifyReply reply = new VerifyReply();
			reply.setResult(result);
			serverContext.send(conn, reply);
		} else {
			throw new InvalidInputException("Key is not a signing key");
		}
		
	}
}

class VerifyReply extends Message implements ReplyMessage {
	private String subject = "Verify";
	private boolean result;
	public void setResult(boolean result2) {
		this.result = result2;
	}
}
