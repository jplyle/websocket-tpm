/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.sign;

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
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.KeyFactory;

/**
 * @author johl
 *
 */
public class Sign extends Message implements IMessage {

	private byte[] data;
	private UUID key;
	
	public byte[] getPlainData() {
		return data;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ox.cs.johnlyle.tpmwebsocket.IMessage#respond(uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerContext, org.java_websocket.WebSocket)
	 */
	@Override
	public void respond(ServerState context, WebSocket conn)
			throws ServerInternalException, InvalidInputException {
		
		if (data == null || key == null) {
			throw new InvalidInputException("Signing requires a key and some data");
		}
		try {
			byte[] signature = sign(KeyFactory.getKeyFactory());
			SignReplyMessage msg = new SignReplyMessage();
			msg.setSignature(signature);
			context.send( conn, msg );
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerInternalException("Error signing", e);
		}
	}

	private byte[] sign(KeyFactory kf) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException, InvalidInputException {
		TPMKey tpmKey = kf.findKeyByUUID(key);
		if (tpmKey instanceof SigningKey) {
			Signer signer = kf.getContext().getSigner();
			byte [] signature = signer.sign(data, (SigningKey) tpmKey);
			return signature;
		} else {
			throw new InvalidInputException("Signing key required for signing");
		}
	}

}
