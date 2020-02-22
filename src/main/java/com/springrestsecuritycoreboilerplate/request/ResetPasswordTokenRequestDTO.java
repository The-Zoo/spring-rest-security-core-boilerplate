package com.springrestsecuritycoreboilerplate.request;

public class ResetPasswordTokenRequestDTO {
	private String usernameOrEmail;

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

}
