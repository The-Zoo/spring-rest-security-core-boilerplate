package com.springrestsecuritycoreboilerplate.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

	public static final String SECRET = "SecretKeyToGenJWTs";
	public static long EXPIRATION_TIME; // 10 days
	public static long EXPIRATION_REFRESH_TIME;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTH = "auth";
	
	@Value("${custom.jwt-token.expired.time}")
	public void setExp(Long exp) {
		SecurityConstants.EXPIRATION_TIME = exp;
	}

	@Value("${custom.jwt-token.expired.refresh.time}")
	public void setRefreshExp(Long refreshExp) { SecurityConstants.EXPIRATION_REFRESH_TIME = refreshExp;	}
}
