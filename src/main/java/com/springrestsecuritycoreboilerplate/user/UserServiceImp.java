package com.springrestsecuritycoreboilerplate.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.AccountNotModifiedException;
import com.springrestsecuritycoreboilerplate.exception.EmailExistsException;
import com.springrestsecuritycoreboilerplate.exception.EmptyValueException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameExistsException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;
import com.springrestsecuritycoreboilerplate.mail.Mailer;
import com.springrestsecuritycoreboilerplate.registration.VerificationToken;
import com.springrestsecuritycoreboilerplate.role.Role;
import com.springrestsecuritycoreboilerplate.role.RoleRepository;
import com.springrestsecuritycoreboilerplate.role.RoleService;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleService;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	private Mailer mailer;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public AppUser saveUser(AppUser appUser) {
		return userRepository.save(appUser);
	}

	@Override
	public Optional<AppUser> findAppUserById(String id) {
		return userRepository.findById(id);
	}

	@Override
	public AppUser getAppUserById(String id) throws AccountNotFoundException {
		Optional<AppUser> foundUser = findAppUserById(id);
		if (foundUser.isPresent() == false) {
			throw new AccountNotFoundException("User is not found by id.");
		}
		return foundUser.get();
	}

	@Override
	public AppUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Iterable<AppUser> allPulses() {
		return userRepository.findAll();

	}

	@Override
	public AppUser addUser(AppUser appUser) throws UsernameFoundException, RoleNotFoundException, EmptyValueException {
		checkAppUserObject(appUser);
		Role foundRole = roleService.findRoleByName(appUser.getRole().getName());
		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		appUser.setRole(foundRole);
		appUser.setCanBeModified(true);
		return saveUser(appUser);
	}

	@Override
	public void deleteUser(String id) throws AccountNotFoundException, AccountNotModifiedException {
		AppUser foundUser = getAppUserById(id);
		if (foundUser.getCanBeModified() == false) {
			throw new AccountNotModifiedException("This account cannot be deleted");
		}
		userRepository.delete(foundUser);

	}

	@Override
	public AppUser updateUser(String id, AppUser appUser) throws EmptyValueException, UsernameFoundException,
			AccountNotModifiedException, AccountNotFoundException, RoleNotFoundException {
		checkAppUserObject(appUser);
		AppUser foundUser = getAppUserById(id);
		Role foundRole = roleService.findRoleByName(appUser.getRole().getName());
		if (foundUser.getCanBeModified() == false) {
			throw new AccountNotModifiedException("This account cannot be modified");
		}
		foundUser.setRole(foundRole);
		foundUser.setUsername(appUser.getUsername());
		foundUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		return saveUser(foundUser);

	}

	private void checkAppUserObject(AppUser appUser) throws EmptyValueException, UsernameFoundException {
		if (!isEmpty(appUser.getUsername()) || !isEmpty(appUser.getPassword())
				|| !isEmpty(appUser.getRole().getName())) {
			throw new EmptyValueException("There is no value to evaluate!");
		}
		if (doesUsernameExist(appUser.getUsername())) {
			throw new UsernameFoundException(appUser.getUsername());
		}
	}

	private boolean isEmpty(String value) {
		return (value != "" && value != null);
	}

	private boolean doesUsernameExist(String username) {
		AppUser foundUser = userRepository.findByUsername(username);
		return foundUser != null;
	}

	private boolean doesEmailExist(String email) {
		AppUser foundUser = userRepository.findByEmail(email);
		return foundUser != null;
	}

	public AppUser getCurrrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		AppUser currentUser = findByUsername(currentPrincipalName);
		return currentUser;
	}

	@Override
	public AppUser registerUser(AppUser appUser) throws EmailExistsException, UsernameExistsException {
		if (doesEmailExist(appUser.getEmail())) {
			throw new EmailExistsException(appUser.getEmail());
		}
		if (doesUsernameExist(appUser.getUsername())) {
			throw new UsernameExistsException(appUser.getUsername());
		}
		appUser.setRole(roleRepository.findByName("ROLE_USER"));
		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		appUser.setToken(new VerificationToken());
	    appUser= saveUser(appUser);
	    mailer.sendRegistrationEmailMessage(appUser, appUser.getToken().getToken());
	    return appUser;
	}

}
