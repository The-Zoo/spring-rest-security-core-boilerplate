package com.springrestsecuritycoreboilerplate.user;

import java.util.Optional;
import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.AccountNotModifiedException;
import com.springrestsecuritycoreboilerplate.exception.EmptyValueException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;

public interface UserService {

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
}
