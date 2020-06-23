package com.springrestsecuritycoreboilerplate.password;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {
	Optional<ResetPasswordToken> findByTokenAndDeleted(String token, boolean deleted);
}
