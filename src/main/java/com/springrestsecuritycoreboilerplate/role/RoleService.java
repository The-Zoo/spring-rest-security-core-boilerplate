package com.springrestsecuritycoreboilerplate.role;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;

public interface RoleService {

	Collection<? extends GrantedAuthority> getAuthorities(final Role role);

	List<String> getPrivileges(final Role role);

	List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges);

	Role findRoleByName(String roleName) throws RoleNotFoundException;

}
