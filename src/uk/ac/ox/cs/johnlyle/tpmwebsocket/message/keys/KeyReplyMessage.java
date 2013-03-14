package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys;

import java.util.UUID;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.keys.SigningKey;
import javax.trustedcomputing.tpm.keys.StorageKey;
import javax.trustedcomputing.tpm.keys.TPMKey;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.ReplyMessage;

class KeyReplyMessage extends Message implements ReplyMessage {

	private String subject = "GenerateKey";
	UUID uuid;
	KeyUsage usage; 
	String format;
	byte[] encoded;
	
	void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	void setAllFromTpmKey(TPMKey tk) throws TrustedComputingException {
		if (tk instanceof SigningKey) {
			setAll((SigningKey) tk);
		} else if (tk instanceof StorageKey) {
			setAll((StorageKey) tk);
		}
	}
	
	void setAll(SigningKey sk) throws TrustedComputingException {
		this.uuid = sk.getUUID();
		this.format = sk.getPublicKey().getFormat();
		this.encoded = sk.getPublicKey().getEncoded();
		this.usage = KeyUsage.Signing;
	}
	
	void setAll(StorageKey sk) throws TrustedComputingException {
		this.uuid = sk.getUUID();
		this.usage = KeyUsage.Storage;
		this.format = sk.getPublicKey().getFormat();
		this.encoded = sk.getPublicKey().getEncoded();		
	}
}