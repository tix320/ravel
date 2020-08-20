package com.github.tix320.ravel.internal.exception;

public class RavelException extends RuntimeException {

	public RavelException() {
		super();
	}

	public RavelException(String message) {
		super(message);
	}

	public RavelException(String message, Throwable cause) {
		super(message, cause);
	}

	public RavelException(Throwable cause) {
		super(cause);
	}

	protected RavelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
