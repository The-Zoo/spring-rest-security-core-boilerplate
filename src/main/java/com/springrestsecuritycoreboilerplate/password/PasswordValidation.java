package com.springrestsecuritycoreboilerplate.password;

import org.passay.AllowedRegexRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.springrestsecuritycoreboilerplate.exception.PasswordValidationException;

@Component
public class PasswordValidation {
	
	@Value("${app.passwordpolicy.minlength}")
	private int minLength;
	
	@Value("${app.passwordpolicy.maxlength}")
	private int maxLength;
	
	@Value("${app.passwordpolicy.constraint.digit}")
	private int numberOfDigit;
	
	@Value("${app.passwordpolicy.constraint.lowercase}")
	private int numberOfLowercase;
	
	@Value("${app.passwordpolicy.constraint.uppercase}")
	private int numberOfUppercase;
	
	@Value("${app.passwordpolicy.constraint.specialcharacter}")
	private int numberOfSpecialCharacter;

	
	public PasswordValidationResult isPasswordValid(String password1, String password2) throws PasswordValidationException {
		if (!password1.equals(password2)) {
			throw new PasswordValidationException("Passwords are not equal!");
		}
		return isPasswordValid(password1);
	}
	
	public PasswordValidationResult isPasswordValid(String password) throws PasswordValidationException {
		PasswordValidationResult passwordValidationResult = new PasswordValidationResult();
		passwordValidationResult.setSuccess(true);
		passwordValidationResult = checkForPasswordRequirements(password, passwordValidationResult);
		if (!passwordValidationResult.isSuccess())
			throw new PasswordValidationException("Password does not meet its requirements", passwordValidationResult);
		return passwordValidationResult;
	}
	
	private PasswordValidationResult checkForPasswordRequirements(String password, PasswordValidationResult passwordValidationResult) {
		return isPasswordValidInConsraints(password, passwordValidationResult);		
	}

	private PasswordValidationResult isPasswordValidInConsraints(String password, PasswordValidationResult passwordValidationResult) {
		PasswordValidator passwordValidator = new PasswordValidator(new LengthRule(minLength, maxLength),
				new CharacterRule(EnglishCharacterData.LowerCase, numberOfLowercase),
				new CharacterRule(EnglishCharacterData.UpperCase, numberOfUppercase), 
				new CharacterRule(EnglishCharacterData.Digit, numberOfDigit),
				new CharacterRule(EnglishCharacterData.Special, numberOfSpecialCharacter), 
				new AllowedRegexRule("^[A-Za-z0-9\\p{Punct}]+$"));
		PasswordData passwordData = new PasswordData();
		passwordData.setPassword(password);
		RuleResult validate = passwordValidator.validate(passwordData);
		passwordValidationResult.setRuleResult(validate);
		passwordValidationResult.setSuccess(validate.isValid());
		return passwordValidationResult;
	}
}
