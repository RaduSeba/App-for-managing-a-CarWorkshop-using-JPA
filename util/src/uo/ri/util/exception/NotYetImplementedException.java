package uo.ri.util.exception;

public class NotYetImplementedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private static final String defaultMessage = "Not yet implemented";

	public NotYetImplementedException() {
		super(defaultMessage);
	}

	public NotYetImplementedException(String message) {
		super(message);
	}

	public NotYetImplementedException(Throwable cause) {
		super(cause);
	}

	public NotYetImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotYetImplementedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
