package com.springrestsecuritycoreboilerplate.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;

@Service
public class RoleServiceImp implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role findRoleByName(String roleName) throws RoleNotFoundException {
		Role foundRole = roleRepository.findByName(roleName);
		if (foundRole == null) {
			throw new RoleNotFoundException(roleName);
		}
		return foundRole;
	}

	@Override
	public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
		return getGrantedAuthorities(getPrivileges(roles));
	}
	//"Set" can be used for preventing duplicate roles/privileges in the future besides that already,
	//During auth processing, Spring is preventing for duplicates with Collections.unmodifiableSet(sortAuthorities(authorities));
	@Override
	public final List<String> getPrivileges(final Collection<Role> roles) {
		final List<String> privileges = new ArrayList<String>();
		final List<Privilege> collection = new ArrayList<Privilege>();
		for (final Role role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (final Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	@Override
	public final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (final String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}
}
