package com.crowdplatform.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.crowdplatform.aux.PasswordResetData;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetDataValidatorTest {

	private PasswordResetDataValidator validator = new PasswordResetDataValidator();
	
	@Test
	public void testValidateRejectsEmptyPassword() {
		PasswordResetData data = new PasswordResetData();
		data.setPassword("");
		Errors errors = Mockito.mock(Errors.class);
	
		validator.validate(data, errors);
		
		Mockito.verify(errors).rejectValue("password", "registration.password.notEmpty", null, null);
	}
	
	@Test
	public void testValidateRejectsWhitespacePassword() {
		PasswordResetData data = new PasswordResetData();
		data.setPassword("  	 ");
		Errors errors = Mockito.mock(Errors.class);
	
		validator.validate(data, errors);
		
		Mockito.verify(errors).rejectValue("password", "registration.password.notEmpty", null, null);
	}
	
	@Test
	public void testValidateRejectsDifferentPasswords() {
		PasswordResetData data = new PasswordResetData();
		data.setPassword("first password");
		data.setConfirmPassword("second password");
		Errors errors = Mockito.mock(Errors.class);
	
		validator.validate(data, errors);
		
		Mockito.verify(errors).rejectValue("confirmPassword", "registration.password.noMatch");
	}
}
