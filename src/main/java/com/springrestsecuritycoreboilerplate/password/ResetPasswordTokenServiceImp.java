package com.springrestsecuritycoreboilerplate.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springrestsecuritycoreboilerplate.exception.InvalidTokenException;

@Service
public class ResetPasswordTokenServiceImp implements ResetPasswordTokenService {

	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;

	@Override
	public ResetPasswordToken findVerificationTokenByIdAndDeletedStatus(String id, Boolean deleted)
			throws InvalidTokenException {
		ResetPasswordToken foundResetPasswordToken = resetPasswordTokenRepository.findByTokenAndDeleted(id, deleted)
				.orElseThrow(() -> new InvalidTokenException("Invalid reset password token with this id: " + id ));
		return foundResetPasswordToken;
	}

}
