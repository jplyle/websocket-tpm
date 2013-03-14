/**
 * 
 */
package uk.ac.ox.cs.johnlyle.tpmwebsocket.exceptions;

/**
 * @author johl
 *
 */
public class ServerInternalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5604155365584673318L;

	/**
	 * 
	 */
	public ServerInternalException() {
	}

	/**
	 * @param message
	 */
	public ServerInternalException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServerInternalException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServerInternalException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ServerInternalException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
