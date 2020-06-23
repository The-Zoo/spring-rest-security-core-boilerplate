package com.springrestsecuritycoreboilerplate.password;

import com.springrestsecuritycoreboilerplate.exception.InvalidTokenException;

public interface ResetPasswordTokenService {

	ResetPasswordToken findVerificationTokenByIdAndDeletedStatus(String id, Boolean deleted)
			throws InvalidTokenException;

}
