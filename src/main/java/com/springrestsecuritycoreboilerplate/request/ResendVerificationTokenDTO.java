package com.springrestsecuritycoreboilerplate.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ResendVerificationTokenDTO {

	@Email
	@NotBlank
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
