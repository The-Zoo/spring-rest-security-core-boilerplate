package com.springrestsecuritycoreboilerplate.user;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.springrestsecuritycoreboilerplate.exception.AccountNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.AccountNotModifiedException;
import com.springrestsecuritycoreboilerplate.exception.EmailExistsException;
import com.springrestsecuritycoreboilerplate.exception.EmptyValueException;
import com.springrestsecuritycoreboilerplate.exception.RoleNotFoundException;
import com.springrestsecuritycoreboilerplate.exception.UsernameExistsException;
import com.springrestsecuritycoreboilerplate.exception.UsernameFoundException;
import com.springrestsecuritycoreboilerplate.response.ResponseObject;

@RestController
public class UserController {

	@Autowired
	UserService userService;

	ResponseObject responseObject;
	
	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public ResponseEntity<Object> registerUser(@RequestBody AppUser appUser) {
		try {
			return new ResponseEntity<>(userService.registerUser(appUser), HttpStatus.CREATED);
		} catch (EmailExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (UsernameExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/api/users", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
	public ResponseEntity<Object> getAllUsers() {
		responseObject = new ResponseObject();
		responseObject.setData(userService.allPulses());
		return new ResponseEntity<>(responseObject, HttpStatus.OK);

	}

	@RequestMapping(value = "/api/user", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
	public ResponseEntity<Object> addUser(@RequestBody AppUser appUser) {
		try {
			return new ResponseEntity<>(userService.addUser(appUser), HttpStatus.OK);
		} catch (UsernameFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (RoleNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (EmptyValueException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}

	}

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

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAuthority('PRIVILEGE_EDIT_USER')")
	public ResponseEntity<Object> updateUserById(@PathVariable("id") String id, @RequestBody AppUser appUser) {
		try {
			return new ResponseEntity<>(userService.updateUser(id, appUser), HttpStatus.OK);
		} catch (EmptyValueException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		} catch (UsernameFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (AccountNotModifiedException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (AccountNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (RoleNotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
