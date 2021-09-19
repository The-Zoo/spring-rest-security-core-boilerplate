package com.springrestsecuritycoreboilerplate.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.*;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.EXPIRATION_TIME;

public final class SecurityUtil {

    public static String createToken(String username, String authorities, boolean isRefresh)
    {
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTH, authorities)
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .setExpiration(new Date(System.currentTimeMillis() + (isRefresh ? EXPIRATION_REFRESH_TIME : EXPIRATION_TIME)))
                .compact();
    }

}
