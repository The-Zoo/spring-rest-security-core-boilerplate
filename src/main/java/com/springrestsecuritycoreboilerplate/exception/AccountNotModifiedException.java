package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class AccountNotModifiedException extends Throwable {

	public AccountNotModifiedException(final String message) {
		super(message);
	}
}