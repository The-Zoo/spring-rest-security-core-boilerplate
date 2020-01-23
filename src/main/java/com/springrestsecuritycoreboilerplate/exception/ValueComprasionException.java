package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class ValueComprasionException extends Throwable {

	public ValueComprasionException(final String message) {
		super("Error Occurred! " + message);
	}

}
