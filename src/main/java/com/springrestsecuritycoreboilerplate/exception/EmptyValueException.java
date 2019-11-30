package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class EmptyValueException extends Throwable {

	public EmptyValueException(final String message) {
		super("There are null or empty values: " + message);
	}
}
