package com.springrestsecuritycoreboilerplate.password;

import org.passay.RuleResult;

public class PasswordValidationResult {
	private boolean isSuccess = true;
	private RuleResult ruleResult;
	private String message;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public RuleResult getRuleResult() {
		return ruleResult;
	}

	public void setRuleResult(RuleResult ruleResult) {
		this.ruleResult = ruleResult;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
