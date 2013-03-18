package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.crowdplatform.model.Registration;
import com.crowdplatform.model.User;
import com.crowdplatform.service.UserService;
import com.crowdplatform.util.RegistrationValidator;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@InjectMocks
	private UserController controller = new UserController();

	@Mock
	private RegistrationValidator validator;
	
	@Mock
	private UserService service;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testShowRegistrationHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.showRegistration(model);
		
		assertEquals("register", result);
	}
	
	@Test
	public void testShowRegistrationAddsEmptyRegistrationToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.showRegistration(model);
		
		Mockito.verify(model).addAttribute(Mockito.any(Registration.class));
	}
	
	@Test
	public void testProcessRegistrationValidatesInput() {
		Registration registration = new Registration();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		
		controller.processRegistration(registration, bindingResult);
		
		Mockito.verify(validator).validate(registration, bindingResult);
	}
	
	@Test
	public void testProcessRegistrationCallsUserService() {
		Registration registration = new Registration();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.processRegistration(registration, bindingResult);
		
		Mockito.verify(service).addUser(Mockito.any(User.class));
		
	}
}
