package com.springrestsecuritycoreboilerplate.exception;

import com.springrestsecuritycoreboilerplate.password.PasswordValidationResult;

@SuppressWarnings("serial")
public class PasswordValidationException extends Throwable {

	private PasswordValidationResult passwordValidationResult;

	public PasswordValidationException(final String message) {
		super("Error Occurred! " + message);
		PasswordValidationResult passwordValidationResult = new PasswordValidationResult();
		passwordValidationResult.setMessage(message);
		passwordValidationResult.setSuccess(false);
		this.passwordValidationResult = passwordValidationResult;
	}

	public PasswordValidationException(final String message, PasswordValidationResult passwordValidationResult) {
		super("Error Occurred! " + message);
		passwordValidationResult.setMessage(message);
		this.passwordValidationResult = passwordValidationResult;

	}

	public PasswordValidationResult getPasswordValidationResult() {
		return passwordValidationResult;
	}
}