package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class InvalidTokenException extends Throwable{
	
	public InvalidTokenException(final String message) {
		super(message);
	}

}
