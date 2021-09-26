package com.springrestsecuritycoreboilerplate.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.response.LoginResponse;
import com.springrestsecuritycoreboilerplate.response.RefreshTokenResponse;
import com.springrestsecuritycoreboilerplate.user.AppUser;
import com.springrestsecuritycoreboilerplate.user.UserService;

import com.springrestsecuritycoreboilerplate.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.*;

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
		String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		User user = (User) auth.getPrincipal();
		String token = SecurityUtil.createToken(user.getUsername(), authorities, false);
		String refreshToken = SecurityUtil.createToken(user.getUsername(), authorities, true);
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
		LoginResponse loginResponse = makeLoginResponse(user, token, refreshToken);
		String jwtResponse = objectMapper.writeValueAsString(loginResponse);
		res.setContentType("application/json");
		res.getWriter().write(jwtResponse);
	}

	private LoginResponse makeLoginResponse(User user, String token, String refreshToken) {
		LoginResponse loginResponse = new LoginResponse();
		AppUser appUser = new AppUser();
		try {
			appUser = userService.findAppUserByUsername(user.getUsername());
		} catch (AccountNotFoundException e) {
			return loginResponse;
		}
		appUser.setPassword(null);
		loginResponse.setUser(appUser);
		loginResponse.setToken(token);
		loginResponse.setRefreshToke(refreshToken);
		return loginResponse;
	}
}
