package com.springrestsecuritycoreboilerplate.password;

import org.springframework.data.repository.CrudRepository;

public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {
}
