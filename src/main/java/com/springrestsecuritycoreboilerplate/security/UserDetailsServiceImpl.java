package com.springrestsecuritycoreboilerplate.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.springrestsecuritycoreboilerplate.role.RoleService;
//import com.weatherapp.security.LoginAttemptService;
import com.springrestsecuritycoreboilerplate.user.AppUser;
import com.springrestsecuritycoreboilerplate.user.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private HttpServletRequest request;

	private UserRepository applicationUserRepository;
	private RoleService roleService;

	public UserDetailsServiceImpl(UserRepository applicationUserRepository, RoleService roleService) {
		this.applicationUserRepository = applicationUserRepository;
		this.roleService = roleService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser applicationUser = applicationUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		Collection<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
		authorityList = (Collection<GrantedAuthority>) roleService.getAuthorities(applicationUser.getRoles());
		return new User(applicationUser.getUsername(), applicationUser.getPassword(), true, true, true, true,
				authorityList);
	}

}
