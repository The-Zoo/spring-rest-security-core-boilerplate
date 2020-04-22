 	package com.springrestsecuritycoreboilerplate.registration;

import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;

public interface VerificationTokenService {

	VerificationToken saveOrUpdateVerificationToken(VerificationToken verificationToken);

	void deleteVerificationToken(VerificationToken verificationToken);

	void deleteVerificationToken(String token);
	
	VerificationToken findVerificationTokenByIdAndDeletedStatus(String id, Boolean deleted)
			throws VerificationTokenNotFoundException;

}
