package org.chai.shared.model.exceptions;

public class DataAccessException extends Exception {

	private static final long serialVersionUID = 8163785566056633422L;

	public DataAccessException() {
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(Throwable throwable) {
		super(throwable);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
