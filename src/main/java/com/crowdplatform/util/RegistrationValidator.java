package com.crowdplatform.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.crowdplatform.aux.Registration;
import com.crowdplatform.service.PlatformUserService;

@Component
public class RegistrationValidator {

	@Autowired
	private PlatformUserService userService;
	
	public void validate(Registration registration, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
		        "registration.username.notEmpty");
		String username = registration.getUsername();
		if (userService.usernameExists(username)) {
			errors.rejectValue("username", "registration.username.exists");
		}
		if (username.length() > 50) {
			errors.rejectValue("username", "registration.username.lengthExceeded");
		}
		if (registration.getPassword() == null || !registration.getPassword().equals(registration.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "registration.password.noMatch");
		}
	}

}
