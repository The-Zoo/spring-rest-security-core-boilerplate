package com.springrestsecuritycoreboilerplate.user;

import javax.validation.Valid;

import com.springrestsecuritycoreboilerplate.response.RefreshTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.AccountNotModifiedException;
import com.springrestsecuritycoreboilerplate.exception.EmailExistsException;
import com.springrestsecuritycoreboilerplate.exception.EmptyValueException;
import com.springrestsecuritycoreboilerplate.exception.ExpiredTokenException;
import com.springrestsecuritycoreboilerplate.exception.InvalidTokenException;
import com.springrestsecuritycoreboilerplate.exception.PasswordValidationException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameExistsException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;
import com.springrestsecuritycoreboilerplate.exception.ValueComprasionException;
import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.VerifiedUserException;
import com.springrestsecuritycoreboilerplate.request.PasswordChangeRequestDTO;
import com.springrestsecuritycoreboilerplate.request.ResendVerificationTokenDTO;
import com.springrestsecuritycoreboilerplate.request.ResetPasswordRequestDTO;
import com.springrestsecuritycoreboilerplate.request.ResetPasswordTokenRequestDTO;
import com.springrestsecuritycoreboilerplate.request.UserRegisterRequestDTO;
import com.springrestsecuritycoreboilerplate.response.ResponseObject;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	ResponseObject responseObject;

	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
		try {
			return new ResponseEntity<>(userService.registerUser(userRegisterRequestDTO), HttpStatus.CREATED);
		} catch (EmailExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (UsernameExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (PasswordValidationException e) {
			return new ResponseEntity<>(e.getPasswordValidationResult(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@RequestMapping(value = "/api/resend-token", method = RequestMethod.POST)
	public ResponseEntity<Object> resendVerificationToken(
			@Valid @RequestBody ResendVerificationTokenDTO resendVerificationTokenDTO) {
		try {
			return new ResponseEntity<>(userService.resendVerificationToken(resendVerificationTokenDTO),
					HttpStatus.CREATED);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (VerifiedUserException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (VerificationTokenNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
	public ResponseEntity<Object> getAllUsers() {
		responseObject = new ResponseObject();
		responseObject.setData(userService.allPulses());
		return new ResponseEntity<>(responseObject, HttpStatus.OK);

	}

//	@RequestMapping(value = "/api/user", method = RequestMethod.POST)
//	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
//	public ResponseEntity<Object> addUser(@RequestBody AppUser appUser) {
//		try {
//			return new ResponseEntity<>(userService.addUser(appUser), HttpStatus.OK);
//		} catch (UsernameFoundException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//		} catch (RoleNotFoundException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		} catch (EmptyValueException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
//		} catch (Exception e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//		}
//
//	}

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
	public ResponseEntity<Object> deleteUserById(@PathVariable("id") String id) {
		try {
			userService.deleteUser(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (AccountNotModifiedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

//	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.PUT)
//	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
//	public ResponseEntity<Object> updateUserById(@PathVariable("id") String id, @RequestBody AppUser appUser) {
//		try {
//			return new ResponseEntity<>(userService.updateUser(id, appUser), HttpStatus.OK);
//		} catch (EmptyValueException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
//		} catch (UsernameFoundException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		} catch (AccountNotModifiedException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		} catch (AccountNotFoundException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		} catch (RoleNotFoundException e) {
//			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//		}
//	}

	@RequestMapping(value = "/api/verify-user/{token}", method = RequestMethod.GET)
	public ResponseEntity<Object> verifyUser(@PathVariable("token") String token) {
		try {
			userService.verifyUser(token);
			return new ResponseEntity<>("User Verified", HttpStatus.ACCEPTED);

		} catch (VerificationTokenNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ExpiredTokenException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (VerifiedUserException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/api/change-password", method = RequestMethod.POST)
	public ResponseEntity<Object> changePassword(
			@Valid @RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
		try {
			return new ResponseEntity<>(userService.changeUserPassword(passwordChangeRequestDTO), HttpStatus.ACCEPTED);
		} catch (ValueComprasionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (PasswordValidationException e) {
			return new ResponseEntity<>(e.getPasswordValidationResult(), HttpStatus.NOT_ACCEPTABLE);
		}

	}

	@RequestMapping(value = "/api/recovery-password", method = RequestMethod.POST)
	public ResponseEntity<Object> recoveryPassword(
			@Valid @RequestBody ResetPasswordTokenRequestDTO resetPasswordTokenRequestDTO) {
		try {
			userService.sendResetPasswordToken(resetPasswordTokenRequestDTO);
			return new ResponseEntity<>("User password recovery mail has been sent.", HttpStatus.ACCEPTED);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/api/reset-password", method = RequestMethod.POST)
	public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
		try {
			return new ResponseEntity<>(userService.resetPassword(resetPasswordRequestDTO), HttpStatus.ACCEPTED);
		} catch (ValueComprasionException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (InvalidTokenException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ExpiredTokenException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (PasswordValidationException e) {
			return new ResponseEntity<>(e.getPasswordValidationResult(), HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@RequestMapping(value = "/api/refresh-token", method = RequestMethod.GET)
	public ResponseEntity<Object> refreshUserToken() {
		try {
			return new ResponseEntity<>(userService.refreshUserToken(), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

}
