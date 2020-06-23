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
	public void deleteVerificationToken(VerificationToken verificationToken) {
		verificationTokenRepository.delete(verificationToken);
	}

	@Override
	public void deleteVerificationToken(String token) {
		verificationTokenRepository.removeByToken(token);
	}

	@Override
	public VerificationToken findVerificationTokenByIdAndDeletedStatus(String id, Boolean deleted)
			throws VerificationTokenNotFoundException {
		VerificationToken foundVerificationToken = verificationTokenRepository.findByTokenAndDeleted(id, false)
				.orElseThrow(() -> new VerificationTokenNotFoundException("Verification token is not valid"));
		return foundVerificationToken;
	}
	
	
	
}
