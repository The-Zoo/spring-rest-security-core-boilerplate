package com.springrestsecuritycoreboilerplate.user;

import java.util.Optional;
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
import com.springrestsecuritycoreboilerplate.response.RefreshTokenResponse;

public interface UserService {

	AppUser registerUser(UserRegisterRequestDTO userRegisterRequestDTO)
			throws EmailExistsException, UsernameExistsException, PasswordValidationException;

	AppUser saveOrUpdateUser(AppUser appUser);

	AppUser findAppUserByUsername(String username) throws AccountNotFoundException;

	Iterable<AppUser> allPulses();

//	AppUser addUser(AppUser appUser) throws UsernameFoundException, RoleNotFoundException, EmptyValueException;

	AppUser findAppUserById(String id) throws AccountNotFoundException;

	void deleteUser(String id) throws AccountNotFoundException, AccountNotModifiedException;

//	AppUser updateUser(String id, AppUser appUser) throws EmptyValueException, UsernameFoundException,
//			AccountNotModifiedException, AccountNotFoundException, RoleNotFoundException;

	AppUser getCurrrentUserByAuth() throws AccountNotFoundException;

	void verifyUser(String token) throws VerificationTokenNotFoundException, AccountNotFoundException,
			ExpiredTokenException, VerifiedUserException;

	AppUser resendVerificationToken(ResendVerificationTokenDTO resendVerificationTokenDTO)
			throws AccountNotFoundException, VerifiedUserException, VerificationTokenNotFoundException;

	AppUser findAppUserByEmail(String email) throws AccountNotFoundException;

	AppUser changeUserPassword(PasswordChangeRequestDTO passwordChangeRequestDTO)
			throws ValueComprasionException, AccountNotFoundException, PasswordValidationException;

	void sendResetPasswordToken(ResetPasswordTokenRequestDTO resetPasswordTokenRequestDTO)
			throws AccountNotFoundException;

	AppUser resetPassword(ResetPasswordRequestDTO resetPasswordRequestDTO)
			throws ValueComprasionException, InvalidTokenException, ExpiredTokenException, PasswordValidationException;

	RefreshTokenResponse refreshUserToken();
}
