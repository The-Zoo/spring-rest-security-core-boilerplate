package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class LocationNotFoundException extends Throwable {
	public LocationNotFoundException(final String message) {
		super("Location not found: " + message);
	}
}
