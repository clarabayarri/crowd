package com.crowdplatform.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.crowdplatform.model.PasswordResetData;

@Component
public class PasswordResetDataValidator {

	public boolean supports(Class<?> klass) {
		return PasswordResetData.class.isAssignableFrom(klass);
	}
	
	public void validate(Object target, Errors errors) {
		PasswordResetData data = (PasswordResetData) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
		        "registration.username.notEmpty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
		        "registration.password.notEmpty");
		if (data.getPassword() != null && !data.getPassword().equals(data.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "registration.password.noMatch");
		}
	}
}
