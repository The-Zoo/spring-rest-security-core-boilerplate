package com.springrestsecuritycoreboilerplate.registration;

import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;
import com.springrestsecuritycoreboilerplate.user.AppUser;

public interface VerificationTokenService {

	VerificationToken saveOrUpdateVerificationToken(VerificationToken verificationToken);

	void deleteVerificationToken(VerificationToken verificationToken);

	void deleteVerificationToken(String token);

}
