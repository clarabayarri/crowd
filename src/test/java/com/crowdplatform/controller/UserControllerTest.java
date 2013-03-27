package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Registration;
import com.crowdplatform.service.UserService;
import com.crowdplatform.util.RegistrationValidator;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@InjectMocks
	private UserController controller = new UserController();

	@Mock
	private AuthenticationManager manager;
	
	@Mock
	private RegistrationValidator validator;
	
	@Mock
	private UserService service;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testLoadLoginHandleRequestView() {
		String result = controller.loadLogin();
		
		assertEquals("login", result);
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
	public void testProcessRegistrationReturnsToRegisterWhenErrors() {
		Registration registration = new Registration();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(true);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		String result = controller.processRegistration(registration, bindingResult, request);
		
		assertEquals("register", result);
	}
	
	@Test
	public void testProcessRegistrationRedirectsWhenCorrect() {
		Registration registration = new Registration();
		registration.setUsername("username");
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		Mockito.when(service.getUser("username")).thenReturn(new PlatformUser());
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		String result = controller.processRegistration(registration, bindingResult, request);
		
		assertEquals("redirect:/projects", result);
	}
	
	@Test
	public void testProcessRegistrationValidatesInput() {
		Registration registration = new Registration();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		controller.processRegistration(registration, bindingResult, request);
		
		Mockito.verify(validator).validate(registration, bindingResult);
	}
	
	@Test
	public void testProcessRegistrationCallsUserService() {
		Registration registration = new Registration();
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		controller.processRegistration(registration, bindingResult, request);
		
		Mockito.verify(service).addUser(Mockito.any(PlatformUser.class));
	}
}
