package com.crowdplatform.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.crowdplatform.model.Registration;

@Component
public class RegistrationValidator {

	public boolean supports(Class<?> klass) {
		return Registration.class.isAssignableFrom(klass);
	}
	
	public void validate(Object target, Errors errors) {
		Registration registration = (Registration) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
		        "registration.username.notEmpty");
		// TODO: check if username exists
		String username = registration.getUsername();
		if (username.length() > 50) {
			errors.rejectValue("username", "registration.username.lengthExceeded");
		}
		if (!registration.getPassword().equals(registration.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "registration.password.noMatch");
		}
	}

}
