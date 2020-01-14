package com.springrestsecuritycoreboilerplate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.springrestsecuritycoreboilerplate.role.RoleService;

import com.springrestsecuritycoreboilerplate.user.AppUser;
import com.springrestsecuritycoreboilerplate.user.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.HEADER_STRING;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.SECRET;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.TOKEN_PREFIX;
import static com.springrestsecuritycoreboilerplate.security.SecurityConstants.AUTH;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private UserRepository userRepository;

	private RoleService roleService;

	public JWTAuthorizationFilter(AuthenticationManager authManager, UserRepository userRepository,
			RoleService roleService) {

		super(authManager);
		this.userRepository = userRepository;
		this.roleService = roleService;

	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);

	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes())
					.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
			String user = claims.getSubject();
			Collection<? extends GrantedAuthority> authorities =
		            Arrays.stream(claims.get(AUTH).toString().split(","))
		                .map(SimpleGrantedAuthority::new)
		                .collect(Collectors.toList());
			
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}
			
//			try {
//				AppUser currentUser = userRepository.findByUsername(user);
//				Collection<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
//				authorityList = (Collection<GrantedAuthority>) roleService.getAuthorities(currentUser.getRole());
//				if (currentUser != null) {
//					return new UsernamePasswordAuthenticationToken(user, null, authorityList);
//				}
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//			}

			return null;
		}
		return null;
	}

}