package com.springrestsecuritycoreboilerplate.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, String> {

	Optional<AppUser> findByEmail(String email);

	Optional<AppUser> findByUsername(String username);

	AppUser findByVerificationTokens_token(String token);
	
	Boolean existsByEmail(String email); 
	
	Boolean existsByUsername(String username); 

}