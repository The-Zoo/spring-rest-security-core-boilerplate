package com.springrestsecuritycoreboilerplate.password;

import com.springrestsecuritycoreboilerplate.config.ApplicationProperties;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springrestsecuritycoreboilerplate.exception.PasswordValidationException;

import java.util.ArrayList;
import java.util.List;

@Component
public class PasswordValidation {
	
	@Autowired
	private ApplicationProperties applicationProperties;

	public PasswordValidationResult isPasswordValid(String password1, String password2, String userName) throws PasswordValidationException {
		if (!password1.equals(password2)) {
			throw new PasswordValidationException("Passwords are not equal!");
		}
		return isPasswordValid(password1, userName);
	}
	
	public PasswordValidationResult isPasswordValid(String password, String userName) throws PasswordValidationException {
		PasswordValidationResult passwordValidationResult = new PasswordValidationResult();
		passwordValidationResult.setSuccess(true);
		passwordValidationResult = checkForPasswordRequirements(password, passwordValidationResult, userName);
		if (!passwordValidationResult.isSuccess())
			throw new PasswordValidationException("Password does not meet its requirements", passwordValidationResult);
		return passwordValidationResult;
	}
	
	private PasswordValidationResult checkForPasswordRequirements(String password, PasswordValidationResult passwordValidationResult, String userName) {
		return isPasswordValidInConsraints(password, passwordValidationResult, userName);		
	}

	private PasswordValidationResult isPasswordValidInConsraints(String password, PasswordValidationResult passwordValidationResult, String userName) {
		List<Rule> ruleList = createRules();
		PasswordValidator passwordValidator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData();
		passwordData.setPassword(password);
		passwordData.setUsername(userName);
		RuleResult validate = passwordValidator.validate(passwordData);
		passwordValidationResult.setRuleResult(validate);
		passwordValidationResult.setSuccess(validate.isValid());
		return passwordValidationResult;
	}

	public List<Rule> createRules () {
		List<Rule> ruleList = new ArrayList<Rule>();
		//TODO: make this part dynamic which get value from config
		ruleList.add(new AllowedRegexRule("^[A-Za-z0-9\\p{Punct}]+$"));
		ruleList.add(new UsernameRule(false, true, MatchBehavior.Contains));
//		new WhitespaceRule();
		if (applicationProperties.getPasswordPolicy() != null) {
			Integer maxLength = null, minLength = null;
			if (applicationProperties.getPasswordPolicy().getMaxlength() != null) {
				maxLength = applicationProperties.getPasswordPolicy().getMaxlength();
			}
			if (applicationProperties.getPasswordPolicy().getMinlength() != null) {
				minLength = applicationProperties.getPasswordPolicy().getMinlength();
			}
			if (minLength != null || maxLength != null) {
				ruleList.add(new LengthRule(minLength != null ? minLength : 0, maxLength != null ? maxLength : 2147483647));
			}
			if (applicationProperties.getPasswordPolicy().getConstraint() != null) {
				if (applicationProperties.getPasswordPolicy().getConstraint().getLowercase() != null) {
					ruleList.add(new CharacterRule(EnglishCharacterData.LowerCase, applicationProperties.getPasswordPolicy().getConstraint().getLowercase()));
				}
				if (applicationProperties.getPasswordPolicy().getConstraint().getUppercase() != null) {
					ruleList.add(new CharacterRule(EnglishCharacterData.UpperCase, applicationProperties.getPasswordPolicy().getConstraint().getUppercase()));
				}
				if (applicationProperties.getPasswordPolicy().getConstraint().getDigit() != null) {
					ruleList.add(new CharacterRule(EnglishCharacterData.Digit, applicationProperties.getPasswordPolicy().getConstraint().getDigit()));
				}
				if (applicationProperties.getPasswordPolicy().getConstraint().getSpecialcharacter() != null) {
					ruleList.add(new CharacterRule(EnglishCharacterData.Special, applicationProperties.getPasswordPolicy().getConstraint().getSpecialcharacter()));
				}
			}
		}
		return ruleList;
	}
}
