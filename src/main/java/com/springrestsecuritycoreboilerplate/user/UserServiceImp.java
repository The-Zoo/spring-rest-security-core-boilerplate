package com.springrestsecuritycoreboilerplate.user;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.AccountNotModifiedException;
import com.springrestsecuritycoreboilerplate.exception.EmailExistsException;
import com.springrestsecuritycoreboilerplate.exception.EmptyValueException;
import com.springrestsecuritycoreboilerplate.exception.ExpiredTokenException;
import com.springrestsecuritycoreboilerplate.exception.InvalidTokenException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameExistsException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;
import com.springrestsecuritycoreboilerplate.exception.ValueComprasionException;
import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.VerifiedUserException;
import com.springrestsecuritycoreboilerplate.mail.Mailer;
import com.springrestsecuritycoreboilerplate.password.ResetPasswordToken;
import com.springrestsecuritycoreboilerplate.password.ResetPasswordTokenRepository;
import com.springrestsecuritycoreboilerplate.password.ResetPasswordTokenService;
import com.springrestsecuritycoreboilerplate.registration.VerificationToken;
import com.springrestsecuritycoreboilerplate.registration.VerificationTokenRepository;
import com.springrestsecuritycoreboilerplate.registration.VerificationTokenService;
import com.springrestsecuritycoreboilerplate.request.PasswordChangeRequestDTO;
import com.springrestsecuritycoreboilerplate.request.ResendVerificationTokenDTO;
import com.springrestsecuritycoreboilerplate.request.ResetPasswordRequestDTO;
import com.springrestsecuritycoreboilerplate.request.ResetPasswordTokenRequestDTO;
import com.springrestsecuritycoreboilerplate.request.UserRegisterRequestDTO;
import com.springrestsecuritycoreboilerplate.role.Role;
import com.springrestsecuritycoreboilerplate.role.RoleRepository;
import com.springrestsecuritycoreboilerplate.role.RoleService;

@Service
public class UserServiceImp implements UserService {
	@Autowired
	VerificationTokenRepository verificationTokenRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ResetPasswordTokenRepository resetPasswordTokenRepository;

	@Autowired
	ResetPasswordTokenService resetPasswordTokenService;

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

//	@Override
//	public AppUser addUser(AppUser appUser) throws UsernameFoundException, RoleNotFoundException, EmptyValueException {
//		checkAppUserObject(appUser);
//		Role foundRole = roleService.findRoleByName(appUser.getRole().getName());
//		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
//		appUser.setRole(foundRole);
//		appUser.setCanBeModified(true);
//		return saveOrUpdateUser(appUser);
//	}

	@Override
	public void deleteUser(String id) throws AccountNotFoundException, AccountNotModifiedException {
		AppUser foundUser = getAppUserById(id);
		if (foundUser.getCanBeModified() == false) {
			throw new AccountNotModifiedException("This account cannot be deleted");
		}
		userRepository.delete(foundUser);

	}

//	@Override
//	public AppUser updateUser(String id, AppUser appUser) throws EmptyValueException, UsernameFoundException,
//			AccountNotModifiedException, AccountNotFoundException, RoleNotFoundException {
//		checkAppUserObject(appUser);
//		AppUser foundUser = getAppUserById(id);
//		Role foundRole = roleService.findRoleByName(appUser.getRole().getName());
//		if (foundUser.getCanBeModified() == false) {
//			throw new AccountNotModifiedException("This account cannot be modified");
//		}
//		foundUser.setRole(foundRole);
//		foundUser.setUsername(appUser.getUsername());
//		foundUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
//		return saveOrUpdateUser(foundUser);
//
//	}

//	private void checkAppUserObject(AppUser appUser) throws EmptyValueException, UsernameFoundException {
//		if (!isEmpty(appUser.getUsername()) || !isEmpty(appUser.getPassword())
//				|| !isEmpty(appUser.getRole().getName())) {
//			throw new EmptyValueException("There is no value to evaluate!");
//		}
//		if (doesUsernameExist(appUser.getUsername())) {
//			throw new UsernameFoundException(appUser.getUsername());
//		}
//	}

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

	@Transactional
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
		appUser.getRoles().add(roleRepository.findByName("ROLE_USER"));
		appUser.setPassword(bCryptPasswordEncoder.encode(userRegisterRequestDTO.getPassword()));
		VerificationToken recreatedVerificationToken = new VerificationToken(appUser);
		appUser.getVerificationToken().add(recreatedVerificationToken);
		appUser = saveOrUpdateUser(appUser);
		mailer.sendVerificationEmailMessage(appUser, recreatedVerificationToken, "Registration Confirmation");
		return appUser;
	}

	@Override
	public void verifyUser(String token) throws VerificationTokenNotFoundException, AccountNotFoundException,
			ExpiredTokenException, VerifiedUserException {
		VerificationToken foundVerificationToken = verificationTokenRepository.findByTokenAndDeleted(token, false);
		if (foundVerificationToken == null) {
			throw new VerificationTokenNotFoundException("Verification token is not valid");
		}
		if ((foundVerificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
			throw new ExpiredTokenException(foundVerificationToken.getExpiryDate());
		}
		AppUser foundUser = foundVerificationToken.getUser();
		if (foundUser == null) {
			throw new AccountNotFoundException("Not found user with token");
		}
		if (foundUser.getVerified()) {
			throw new VerifiedUserException(foundVerificationToken.getUser().getEmail());
		}
		foundUser.setVerified(true);
		foundVerificationToken.setDeleted(true);
		saveOrUpdateUser(foundUser);
	}

	@Override
	public AppUser resendVerificationToken(ResendVerificationTokenDTO resendVerificationTokenDTO)
			throws AccountNotFoundException, VerifiedUserException, VerificationTokenNotFoundException {
		AppUser foundUser = findUserByEmail(resendVerificationTokenDTO.getEmail());
		if (foundUser == null)
			throw new AccountNotFoundException(resendVerificationTokenDTO.getEmail());

		if (foundUser.getVerified())
			throw new VerifiedUserException(foundUser.getEmail());

		if (foundUser.getVerificationToken() == null)
			throw new VerificationTokenNotFoundException("NOT FOUND");

		foundUser.getVerificationToken().forEach(token -> {
			if (token.getDeleted() == false) {
				token.setDeleted(true);
			}
		});
		VerificationToken recreatedToken = new VerificationToken(foundUser);
		foundUser.getVerificationToken().add(recreatedToken);
		mailer.sendVerificationEmailMessage(foundUser, recreatedToken, "Resend Verify Token");
		foundUser = saveOrUpdateUser(foundUser);
		return foundUser;
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

	@Override
	public void sendResetPasswordToken(ResetPasswordTokenRequestDTO resetPasswordTokenRequestDTO)
			throws AccountNotFoundException {
		AppUser foundUser = null;
		if (GenericValidator.isEmail(resetPasswordTokenRequestDTO.getUsernameOrEmail())) {
			foundUser = findUserByEmail(resetPasswordTokenRequestDTO.getUsernameOrEmail());
		} else {
			foundUser = findByUsername(resetPasswordTokenRequestDTO.getUsernameOrEmail());
		}
		if (foundUser == null) {
			throw new AccountNotFoundException("Account is not found");
		}
		foundUser.getResetPasswordTokens().forEach(token -> {
			if (token.getDeleted() == false) {
				token.setDeleted(true);
			}
		});
		ResetPasswordToken createdResetPasswordToken = new ResetPasswordToken(foundUser);
		foundUser.getResetPasswordTokens().add(createdResetPasswordToken);
		foundUser = saveOrUpdateUser(foundUser);
		mailer.sendResetPasswordEmailMessage(foundUser, createdResetPasswordToken, "Reset Password Request");
	}

	@Override
	public AppUser resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO)
			throws ValueComprasionException, InvalidTokenException, ExpiredTokenException {
		if (!resetPasswordRequestDTO.getNewPassword1().equals(resetPasswordRequestDTO.getNewPassword2())) {
			throw new ValueComprasionException("Passwords are not equal");
		}
		ResetPasswordToken foundResetPasswordToken = resetPasswordTokenRepository
				.findByTokenAndDeleted(resetPasswordRequestDTO.getToken(), false);
		if (foundResetPasswordToken == null) {
			throw new InvalidTokenException("Invalid reset password token.");
		}
		if ((foundResetPasswordToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
			throw new ExpiredTokenException(foundResetPasswordToken.getExpiryDate());
		}
		AppUser foundUser = foundResetPasswordToken.getUser();
		foundUser.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequestDTO.getNewPassword1()));
		foundResetPasswordToken.setDeleted(true);
		foundUser = saveOrUpdateUser(foundUser);
		return foundUser;
	}

};
