package ww.midnite.util.http;

import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;


public class HttpGetException extends Exception {

	private static final long serialVersionUID = -6639377471579561645L;

	public static enum Type {
		TIMEOUT, INTERRUPTED, UNKNOWN;
	}

	private final Type type;


	public HttpGetException(final Exception e) {
		super(e.getMessage(), e);

		if (e instanceof SocketTimeoutException) {
			type = Type.TIMEOUT;

		} else if ("Connection timed out".equals(getMessage())) {
			type = Type.TIMEOUT;

		} else if (e instanceof InterruptedIOException) {
			type = Type.INTERRUPTED;

		} else if ("disconnected".equals(getMessage()) ||
		/**/"socket closed".equals(getMessage()) ||
		/**/"stream is closed".equals(getMessage())) {
			type = Type.INTERRUPTED;

		} else {
			type = Type.UNKNOWN;
		}
	}


	public Type getType() {
		return type;
	}


	@Override
	public String toString() {
		return type.name() + (type == Type.UNKNOWN && getCause() != null ? " (" + getCause().getClass().getSimpleName() + ") " : " ") + getMessage();
	}

}
