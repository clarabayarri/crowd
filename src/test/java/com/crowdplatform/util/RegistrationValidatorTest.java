package com.crowdplatform.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import com.crowdplatform.aux.Registration;
import com.crowdplatform.service.PlatformUserService;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationValidatorTest {

	@InjectMocks
	private RegistrationValidator validator = new RegistrationValidator();
	
	@Mock
	private PlatformUserService userService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testValidateRejectsEmptyUsername() {
		Registration registration = new Registration();
		registration.setUsername("");
		registration.setPassword("");
		registration.setConfirmPassword("");
		Errors errors = Mockito.mock(Errors.class);
	
		validator.validate(registration, errors);
		
		Mockito.verify(errors).rejectValue("username", "registration.username.notEmpty", null, null);
	}
	
	@Test
	public void testValidateRejectsWhitespaceUsername() {
		Registration registration = new Registration();
		registration.setUsername("     	");
		registration.setPassword("");
		registration.setConfirmPassword("");
		Errors errors = Mockito.mock(Errors.class);
	
		validator.validate(registration, errors);
		
		Mockito.verify(errors).rejectValue("username", "registration.username.notEmpty", null, null);
	}
	
	@Test
	public void testValidateRejectsExistingUsername() {
		Registration registration = new Registration();
		String username = "username";
		registration.setUsername(username);
		registration.setPassword("");
		registration.setConfirmPassword("");
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(userService.usernameExists(username)).thenReturn(true);
	
		validator.validate(registration, errors);
		
		Mockito.verify(errors).rejectValue("username", "registration.username.exists");
	}
	
	@Test
	public void testValidateRejectsLongUsername() {
		Registration registration = new Registration();
		String username = "usernameusernameusernameusernameusernameusernameusername";
		registration.setUsername(username);
		registration.setPassword("");
		registration.setConfirmPassword("");
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(userService.usernameExists(username)).thenReturn(false);
	
		validator.validate(registration, errors);
		
		Mockito.verify(errors).rejectValue("username", "registration.username.lengthExceeded");
	}
	
	@Test
	public void testValidateRejectsNullPassword() {
		Registration registration = new Registration();
		String username = "username";
		registration.setUsername(username);
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(userService.usernameExists(username)).thenReturn(false);
	
		validator.validate(registration, errors);
		
		Mockito.verify(errors).rejectValue("confirmPassword", "registration.password.noMatch");
	}
	
	@Test
	public void testValidateRejectsDifferentPasswords() {
		Registration registration = new Registration();
		String username = "username";
		registration.setUsername(username);
		registration.setPassword("fist password");
		registration.setConfirmPassword("second password");
		Errors errors = Mockito.mock(Errors.class);
		Mockito.when(userService.usernameExists(username)).thenReturn(false);
	
		validator.validate(registration, errors);
		
		Mockito.verify(errors).rejectValue("confirmPassword", "registration.password.noMatch");
	}
}
