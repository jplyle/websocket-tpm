package uk.ac.ox.cs.johnlyle.tpmwebsocket.message.pcrs;

import java.util.HashMap;
import java.util.Map;

import javax.trustedcomputing.TrustedComputingException;
import javax.trustedcomputing.tpm.structures.PCRInfo;

import org.java_websocket.WebSocket;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.ServerState;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidInputException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.ServerInternalException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.IMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.Message;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.ReplyMessage;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.KeyFactory;

public class GetPCRValues extends Message implements IMessage {


	
	@Override
	public void respond(ServerState context, WebSocket conn)
			throws ServerInternalException, InvalidInputException,
			ClassCastException, ClassNotFoundException, InstantiationException,
			IllegalAccessException, TrustedComputingException {
		
		KeyFactory kf = KeyFactory.getKeyFactory();
		int numberPcrs = kf.getContext().getTPMInstance().getNumberPCR();
		int[] pcrIndex = new int[numberPcrs];
		for (int i=0; i< numberPcrs; i++) {
			pcrIndex[i] = i;
		}
		PCRInfo info = kf.getContext().getTPMInstance().readPCR(pcrIndex);
		PCRInfoMessage msg = new PCRInfoMessage();
		msg.fill(info);
		context.send(conn, msg);
	}
	
}

class PCRInfoMessage extends Message implements ReplyMessage {

	private String subject = "GetPCRValues";
	
	private Map<Integer, byte[]> pcrValues = new HashMap<Integer, byte[]>();
	
	public void fill(PCRInfo info) {
		for (int i : info.getValueIndices()) {
			pcrValues.put(new Integer(i), info.getPCRValue(i).getBytes());
		}
	}
	
	
	
}
