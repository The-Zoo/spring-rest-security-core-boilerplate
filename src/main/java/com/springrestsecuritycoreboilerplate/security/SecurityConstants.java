package com.springrestsecuritycoreboilerplate.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {

	public static final String SECRET = "SecretKeyToGenJWTs";
	public static long EXPIRATION_TIME; // 10 days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	
	@Value("${custom.jwt-token.expired.time}")
	public void setExp(Long exp) {
		SecurityConstants.EXPIRATION_TIME = exp;
	}
}
