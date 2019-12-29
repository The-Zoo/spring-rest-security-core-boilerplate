package com.springrestsecuritycoreboilerplate.exception;


@SuppressWarnings("serial")
public class EmailExistsException extends Throwable {

    public EmailExistsException(final String email) {
        super("There is an account with that email: " + email);
    }

}