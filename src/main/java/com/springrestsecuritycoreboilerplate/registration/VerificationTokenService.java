 	package com.springrestsecuritycoreboilerplate.registration;

public interface VerificationTokenService {

	VerificationToken saveOrUpdateVerificationToken(VerificationToken verificationToken);

	void deleteVerificationToken(VerificationToken verificationToken);

	void deleteVerificationToken(String token);

}
