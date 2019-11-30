package com.springrestsecuritycoreboilerplate.exception;

@SuppressWarnings("serial")
public class RoleNotFoundException extends Throwable {

	public RoleNotFoundException(final String roleName) {
		super("There is no role with: " + roleName);
	}

}
