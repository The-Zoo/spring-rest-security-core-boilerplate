package com.springrestsecuritycoreboilerplate.user;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
import com.springrestsecuritycoreboilerplate.exception.ExpiredTokenException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameExistsException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;
import com.springrestsecuritycoreboilerplate.exception.ValueComprasionException;
import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.VerifiedUserException;
import com.springrestsecuritycoreboilerplate.mail.Mailer;
import com.springrestsecuritycoreboilerplate.registration.VerificationToken;
import com.springrestsecuritycoreboilerplate.registration.VerificationTokenService;
import com.springrestsecuritycoreboilerplate.request.PasswordChangeRequestDTO;
import com.springrestsecuritycoreboilerplate.request.ResendVerificationTokenDTO;
import com.springrestsecuritycoreboilerplate.request.UserRegisterRequestDTO;
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

	@Autowired
	private VerificationTokenService verificationTokenService;

	@Override
	public AppUser saveOrUpdateUser(AppUser appUser) {
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
	public AppUser findUserByEmail(String email) {
		return userRepository.findByEmail(email);
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
		return saveOrUpdateUser(appUser);
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
		return saveOrUpdateUser(foundUser);

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

	public AppUser getCurrrentUserByAuth() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		AppUser currentUser = findByUsername(currentPrincipalName);
		return currentUser;
	}

	@Override
	public AppUser registerUser(UserRegisterRequestDTO userRegisterRequestDTO)
			throws EmailExistsException, UsernameExistsException {
		if (doesEmailExist(userRegisterRequestDTO.getEmail())) {
			throw new EmailExistsException(userRegisterRequestDTO.getEmail());
		}
		if (doesUsernameExist(userRegisterRequestDTO.getUsername())) {
			throw new UsernameExistsException(userRegisterRequestDTO.getUsername());
		}
		AppUser appUser = new AppUser();
		appUser.setUsername(userRegisterRequestDTO.getUsername());
		appUser.setEmail(userRegisterRequestDTO.getEmail());
		appUser.setRole(roleRepository.findByName("ROLE_USER"));
		appUser.setPassword(bCryptPasswordEncoder.encode(userRegisterRequestDTO.getPassword()));
		appUser.setVerificationToken(new VerificationToken());

		appUser = saveOrUpdateUser(appUser);
		mailer.sendVerificationEmailMessage(appUser, "Registration Confirmation");
		return appUser;
	}

	@Override
	public void verifyUser(String token) throws AccountNotFoundException, ExpiredTokenException, VerifiedUserException {
		AppUser foundUser = userRepository.findByVerificationToken_token(token);
		if (foundUser == null) {
			throw new AccountNotFoundException("Not found user with token");
		}
		if (foundUser.getVerified()) {
			throw new VerifiedUserException(foundUser.getEmail());
		}
		if ((foundUser.getVerificationToken().getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
			throw new ExpiredTokenException(foundUser.getVerificationToken().getExpiryDate());
		}
		foundUser.setVerified(true);
		foundUser.setVerificationToken(null);
		saveOrUpdateUser(foundUser);
		verificationTokenService.deleteVerificationToken(token);
	}

	@Override
	public AppUser resendVerificationToken(ResendVerificationTokenDTO resendVerificationTokenDTO)
			throws AccountNotFoundException, VerifiedUserException, VerificationTokenNotFoundException {
		AppUser foundUser = findUserByEmail(resendVerificationTokenDTO.getEmail());
		if (foundUser == null)
			throw new AccountNotFoundException(resendVerificationTokenDTO.getEmail());

		if (foundUser.getVerified())
			throw new VerifiedUserException(foundUser.getEmail());

		resendVerificationToken(foundUser);
		return foundUser;
	}

	private void resendVerificationToken(AppUser user) throws VerificationTokenNotFoundException {
		VerificationToken updatedVerificationToken = verificationTokenService.updateToken(user);
		mailer.sendVerificationEmailMessage(updatedVerificationToken.getUser(), "Resend Verify Token");
	}

	@Override
	public AppUser changeUserPassword(PasswordChangeRequestDTO passwordChangeRequestDTO)
			throws ValueComprasionException, AccountNotFoundException {
		if (!passwordChangeRequestDTO.getNewPassword1().equals(passwordChangeRequestDTO.getNewPassword2())) {
			throw new ValueComprasionException("Passwords are not equal");
		}
		AppUser foundUser = getAppUserById(passwordChangeRequestDTO.getUserId());
		if (foundUser == null) {
			throw new AccountNotFoundException("Account is not found");
		}
		if (!foundUser.getUsername().equals(getCurrrentUsernameByAuth())) {
			throw new ValueComprasionException("Auth Failed!");
		}
		if (!bCryptPasswordEncoder.matches(passwordChangeRequestDTO.getCurrentPassword(), foundUser.getPassword())) {
			throw new ValueComprasionException("Old password is not correct");
		}
		foundUser.setPassword(bCryptPasswordEncoder.encode(passwordChangeRequestDTO.getNewPassword1()));
		return saveOrUpdateUser(foundUser);
	}

	public String getCurrrentUsernameByAuth() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
}
