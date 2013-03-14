package uk.ac.ox.cs.johnlyle.tpmwebsocket.message;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidMessageException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.keys.GenerateKey;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.pcrs.GetPCRValues;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.sign.Sign;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.sign.Verify;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.store.Seal;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MessageFactory {

	@SuppressWarnings("rawtypes")
	private static Class[] messageClasses = {
				Register.class,
				Sign.class,
				GenerateKey.class,
				GetPCRValues.class,
				Seal.class,
				Verify.class
	};
	

	@SuppressWarnings("unchecked")
	public static IMessage fromString(String message) throws InvalidMessageException {
		Gson gson = new Gson();
		try {
			SubjectMessage basic = gson.fromJson(message, SubjectMessage.class);
			if (basic.getSubject() == null) {
				throw new InvalidMessageException("No subject field");
			}
			for (@SuppressWarnings("rawtypes") Class c : MessageFactory.messageClasses) {
				if (c.getSimpleName().equalsIgnoreCase(basic.getSubject())) {
					return (IMessage) gson.fromJson(message, c);
				}
			}
		} catch (JsonSyntaxException e) {
			throw new InvalidMessageException("Error parsing JSON from WebSocket", e);
		}

		throw new InvalidMessageException("Could not find matching class");
	}
	
	
	
}
