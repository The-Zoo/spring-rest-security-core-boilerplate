package com.springrestsecuritycoreboilerplate.registration;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {

	@Transactional
	Long removeByToken(String Token);
}
