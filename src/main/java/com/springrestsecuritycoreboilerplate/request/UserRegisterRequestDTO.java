package com.springrestsecuritycoreboilerplate.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserRegisterRequestDTO {

	@NotBlank
	String username;
	@NotBlank
	String password;
	@Email
	@NotBlank
	String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
