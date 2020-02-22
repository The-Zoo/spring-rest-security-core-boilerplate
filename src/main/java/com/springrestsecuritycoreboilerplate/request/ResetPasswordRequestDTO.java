package com.springrestsecuritycoreboilerplate.request;

import javax.validation.constraints.NotBlank;

public class ResetPasswordRequestDTO {
	@NotBlank
	private String token;
	@NotBlank
	String newPassword1;
	@NotBlank
	String newPassword2;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String password) {
		this.newPassword1 = password;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String verifyPassword) {
		this.newPassword2 = verifyPassword;
	}

}
