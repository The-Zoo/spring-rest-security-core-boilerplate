package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class VerificationTokenNotFoundException extends Throwable {

	public VerificationTokenNotFoundException(final String message) {
		super("There is no token: " + message);
	}

}