/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions.InvalidMessageException;
import uk.ac.ox.cs.johnlyle.tpmwebsocket.message.MessageFactory;

/**
 * @author johl
 *
 */
public class TpmWebSocketServer extends WebSocketServer {

	ServerState handler;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int port = 5050;
			if (args.length > 0) {
				port = Integer.parseInt(args[0]);
			}
			TpmWebSocketServer server = new TpmWebSocketServer(port);
			server.start();
			System.out.println("Started Tpm WebSocket Server on port " + server.getPort());
		} catch (NumberFormatException e) {
			System.out.println("Failed  to supply a valid port");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
			e.printStackTrace();
		}
	}
	
	public TpmWebSocketServer( int port ) throws UnknownHostException {
		super ( new InetSocketAddress(port) );
		handler = new ServerState();
		
	}
	
	public TpmWebSocketServer( int port, List<Draft> drafts ) throws UnknownHostException {
		super ( new InetSocketAddress(port), drafts );
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("Websocket opened.");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("Websocket closed, code: " + code + ", reason: " + reason + ", remote? " + remote );
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("Received: " + message);
		try {
			MessageFactory.fromString(message);
			handler.handle(conn, MessageFactory.fromString(message));
		} catch (InvalidMessageException e) {
			System.out.println("Invalid message, closing");
			e.printStackTrace();
			conn.close(1003, e.toString()); // http://tools.ietf.org/html/rfc6455#section-7.4
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.out.println("Error: ");
		ex.printStackTrace();
	}
}
