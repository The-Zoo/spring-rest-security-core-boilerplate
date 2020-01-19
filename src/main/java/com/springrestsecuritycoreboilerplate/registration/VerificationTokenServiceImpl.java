package com.springrestsecuritycoreboilerplate.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springrestsecuritycoreboilerplate.exception.VerificationTokenNotFoundException;
import com.springrestsecuritycoreboilerplate.user.AppUser;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

	@Autowired
	VerificationTokenRepository verificationTokenRepository;

	@Override
	public VerificationToken saveOrUpdateVerificationToken(VerificationToken verificationToken) {
		return verificationTokenRepository.save(verificationToken);
	}

	@Override
	public VerificationToken updateToken(AppUser appUser) throws VerificationTokenNotFoundException {
		if(appUser.getToken()==null)
			throw new VerificationTokenNotFoundException("NOT FOUND");
		VerificationToken oldToken = appUser.getToken();
		oldToken.updateToken();
		return saveOrUpdateVerificationToken(oldToken);
	}
}
