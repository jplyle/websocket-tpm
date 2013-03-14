package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.store;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.ReplyMessage;

public class SealReplyMessage extends Message implements ReplyMessage {

	private String subject = "Seal";
	private byte[] sealedData;
	
	public byte[] getResult() {
		return sealedData;
	}
	
	public void setResult(byte[] sealedData) {
		this.sealedData = sealedData;
	}
	
}
