package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.store;

import iaik.tc.jsr321.tpm.structures.DigestImpl;
import iaik.tc.jsr321.tpm.structures.PCRInfoImpl;

import java.util.Map;
import java.util.UUID;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.keys.StorageKey;
import javax.trustedcomputing.tpm.keys.TPMKey;
import javax.trustedcomputing.tpm.structures.Digest;
import javax.trustedcomputing.tpm.structures.PCRInfo;
import javax.trustedcomputing.tpm.structures.Secret;
import javax.trustedcomputing.tpm.tools.Sealer;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.KeyFactory;

public class Seal extends Message implements IMessage {

	private byte[] data;
	private UUID key;
	private Map<Integer, byte[]> pcrState;
	
	@Override
	public void respond(ServerState context, WebSocket conn)
			throws ServerInternalException, InvalidInputException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, TrustedComputingException {
		
		if (data == null || key == null) {
			throw new InvalidInputException("Sealing requires arguments 'data' and 'key'");
		}
		
		KeyFactory kf = KeyFactory.getKeyFactory();
		TPMKey storageKey = kf.findKeyByUUID(key);
		if (storageKey instanceof StorageKey) {
			
			SealReplyMessage msg = new SealReplyMessage();
			msg.setResult( seal(kf, (StorageKey)storageKey) );
			context.send(conn, msg);
			
		} else {
			throw new InvalidInputException("Invalid key selected: must be a storage key");
		}

	}

	private byte[] seal(KeyFactory kf, StorageKey storageKey) throws TrustedComputingException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Sealer sealer = kf.getContext().getSealer();
		
		PCRInfo info = null;
		if (pcrState != null) {
			info = new PCRInfoImpl();
			for (Integer pcr : pcrState.keySet()) {
				Digest digest = new DigestImpl(pcrState.get(pcr)); 
				info.setPCRValue(pcr.intValue(), digest);	
			}
		}
		System.out.println("Sealing, with PCR state: " + new Gson().toJson(info));
		
		byte[] sealed = sealer.seal(data, info, storageKey, Secret.WELL_KNOWN_SECRET);
		return sealed;
	}


}
