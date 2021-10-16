package com.springrestsecuritycoreboilerplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springrestsecuritycoreboilerplate.common.UserRoleConstants;
import com.springrestsecuritycoreboilerplate.role.Privilege;
import com.springrestsecuritycoreboilerplate.role.PrivilegeRepository;
import com.springrestsecuritycoreboilerplate.role.Role;
import com.springrestsecuritycoreboilerplate.role.RoleRepository;
import com.springrestsecuritycoreboilerplate.user.AppUser;
import com.springrestsecuritycoreboilerplate.user.UserRepository;

@Service
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;
		Privilege p1 = createPrivilegeIfNotFound("p1");
		Privilege p2 = createPrivilegeIfNotFound("p2");
		Privilege p3 = createPrivilegeIfNotFound("p3");
		Privilege p4 = createPrivilegeIfNotFound("p4");

		List<Privilege> adminPrivileges = Arrays.asList(p1, p2, p3, p4);
		List<Privilege> userPrivileges = Arrays.asList(p2, p4);

		createRoleIfNotFound(UserRoleConstants.ROLE_ADMIN, adminPrivileges);
		createRoleIfNotFound(UserRoleConstants.ROLE_USER, userPrivileges);

		Role adminRole = roleRepository.findByName(UserRoleConstants.ROLE_ADMIN);
		Role userRole = roleRepository.findByName(UserRoleConstants.ROLE_USER);

		AppUser admin = new AppUser();
		admin.setName("root");
		admin.setUsername("root");
		admin.setPassword(passwordEncoder.encode("root"));
		admin.setRoles(Set.of(adminRole));
		admin.setCanBeModified(false);
		admin.setEmail("root@root.com");
		userRepository.save(admin);

		AppUser standartUser = new AppUser();
		standartUser.setName("user");
		standartUser.setUsername("user");
		standartUser.setPassword(passwordEncoder.encode("user"));
		standartUser.setCanBeModified(true);
		standartUser.setRoles(Set.of(userRole));
		standartUser.setEmail("user@user.com");
		userRepository.save(standartUser);

		alreadySetup = true;
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
		return role;
	}

}
