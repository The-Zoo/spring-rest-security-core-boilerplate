package com.springrestsecuritycoreboilerplate.user;

import java.util.Optional;
import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.AccountNotModifiedException;
import com.springrestsecuritycoreboilerplate.exception.EmailExistsException;
import com.springrestsecuritycoreboilerplate.exception.EmptyValueException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameExistsException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;
import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.VerifiedUserException;
import com.springrestsecuritycoreboilerplate.request.ResendVerificationTokenDTO;
import com.springrestsecuritycoreboilerplate.request.UserRegisterRequestDTO;

public interface UserService {

	AppUser registerUser(UserRegisterRequestDTO userRegisterRequestDTO) throws EmailExistsException, UsernameExistsException;
	
	AppUser saveUser(AppUser appUser);

	AppUser findByUsername(String username);

	Iterable<AppUser> allPulses();

	AppUser addUser(AppUser appUser) throws UsernameFoundException, RoleNotFoundException, EmptyValueException;

	Optional<AppUser> findAppUserById(String id);

	AppUser getAppUserById(String id) throws AccountNotFoundException;

	void deleteUser(String id) throws AccountNotFoundException, AccountNotModifiedException;

	AppUser updateUser(String id, AppUser appUser) throws EmptyValueException, UsernameFoundException,
			AccountNotModifiedException, AccountNotFoundException, RoleNotFoundException;

	AppUser getCurrrentUser();
	
	AppUser resendVerificationToken(ResendVerificationTokenDTO resendVerificationTokenDTO) throws AccountNotFoundException, VerifiedUserException, VerificationTokenNotFoundException;

	AppUser findUserByEmail(String email);
}
