package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class VerifiedUserException extends Throwable {

	public VerifiedUserException(final String email) {
		super("This user is already active: " + email);
	}

}