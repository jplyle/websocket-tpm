/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.sign;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.ReplyMessage;

/**
 * @author johl
 *
 */
public class SignReplyMessage extends Message implements ReplyMessage {

	private String subject = "Sign";
	private byte[] signature;

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	public byte[] getSignature() {
		return this.signature;
	}

}
