package com.springrestsecuritycoreboilerplate.request;

import javax.validation.constraints.Email;

public class ResetPasswordTokenRequestDTO {
	String usernameOrEmail;

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

}
