package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class LocationFoundException extends Throwable {
	public LocationFoundException(final String message) {
		super("Location is found: " + message);
	}

}
