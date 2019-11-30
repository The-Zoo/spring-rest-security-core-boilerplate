package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class UsernameFoundException extends Throwable {

    public UsernameFoundException(final String username) {
        super("There is an account with that username: " + username);
    }

}