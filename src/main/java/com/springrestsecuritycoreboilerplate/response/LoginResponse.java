package com.springrestsecuritycoreboilerplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springrestsecuritycoreboilerplate.user.AppUser;

public class LoginResponse {

	@JsonProperty("status")
	private String statusCode;

	@JsonProperty("data")
	private AppUser user;

//	@JsonProperty("token")
//	@JsonIgnore
	private String token;

	private String refreshToken;

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToke(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
