package com.springrestsecuritycoreboilerplate.registration;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {

	@Transactional
	Long removeByToken(String Token);

	Optional<VerificationToken> findByTokenAndDeleted(String Token, Boolean Deleted);
}
