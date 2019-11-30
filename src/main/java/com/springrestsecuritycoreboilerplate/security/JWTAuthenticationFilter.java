package com.springrestsecuritycoreboilerplate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springrestsecuritycoreboilerplate.response.LoginResponse;
import com.springrestsecuritycoreboilerplate.user.AppUser;
import com.springrestsecuritycoreboilerplate.user.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.EXPIRATION_TIME;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.HEADER_STRING;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.SECRET;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	private ObjectMapper objectMapper;
	private UserService userService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
			UserService userService) {
		this.authenticationManager = authenticationManager;
		this.objectMapper = objectMapper;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			AppUser creds = new ObjectMapper().readValue(req.getInputStream(), AppUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		User user = (User) auth.getPrincipal();
		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, SECRET.getBytes()).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
		LoginResponse loginResponse = makeLoginResponse(user);
		String jwtResponse = objectMapper.writeValueAsString(loginResponse);
		res.setContentType("application/json");
		res.getWriter().write(jwtResponse);
	}

	private LoginResponse makeLoginResponse(User user) {
		LoginResponse loginResponse = new LoginResponse();
		AppUser appUser = new AppUser();
		appUser = userService.findByUsername(user.getUsername());
		appUser.setPassword(null);
		loginResponse.setUser(appUser);
		return loginResponse;
	}
}
