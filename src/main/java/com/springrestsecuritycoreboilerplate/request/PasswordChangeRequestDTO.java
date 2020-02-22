package com.springrestsecuritycoreboilerplate.request;

import javax.validation.constraints.NotBlank;

public class PasswordChangeRequestDTO {

	@NotBlank
	private String userId;
	@NotBlank
	private String currentPassword;
	@NotBlank
	private String newPassword1;
	@NotBlank
	private String newPassword2;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}

}
