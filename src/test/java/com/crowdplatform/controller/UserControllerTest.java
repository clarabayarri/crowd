package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.crowdplatform.model.PasswordResetData;
import com.crowdplatform.model.PasswordResetRequest;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Registration;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.util.MailSender;
import com.crowdplatform.util.PasswordResetDataValidator;
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
	private PasswordResetDataValidator passwordResetValidator;
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private PasswordEncoder encoder;
	
	@Mock
	private MailSender mailSender;
	
	private static final Long uid = new Long(2);
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(encoder.encodePassword(Mockito.anyString(), Mockito.isNull())).thenReturn("password");
	}
	
	@Test
	public void testLoadLoginHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.loadLogin(model, null);
		
		assertEquals("login", result);
	}
	
	@Test
	public void testLoadLoginAddsErrorParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.loadLogin(model, true);
		
		Mockito.verify(model).addAttribute("error", true);
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
		Mockito.when(userService.getUser("username")).thenReturn(new PlatformUser());
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		
		String result = controller.processRegistration(registration, bindingResult, request);
		
		assertEquals("redirect:/projects?registered=true", result);
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
		
		Mockito.verify(userService).addUser(Mockito.any(PlatformUser.class));
	}
	
	@Test
	public void testForgotPasswordRetrievesUserByUsernameOrEmail() {
		controller.forgotPassword("username");
		
		Mockito.verify(userService).getUserByUsernameOrEmail("username");
	}
	
	@Test
	public void testForgotPasswordCreatesRequestIfUserExists() {
		PlatformUser user = new PlatformUser();
		Mockito.when(userService.getUserByUsernameOrEmail("username")).thenReturn(user);
		
		controller.forgotPassword("username");
		
		Mockito.verify(userService).saveUser(user);
	}
	
	@Test
	public void testForgotPasswordDoesntCreateRequestIfUserDoesntExist() {
		controller.forgotPassword("username");
		
		Mockito.verify(userService, Mockito.never()).saveUser(Mockito.any(PlatformUser.class));
	}
	
	@Test
	public void testLoadPasswordResetHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.loadPasswordReset(new Long(2), model);
		
		assertEquals("password-reset", result);
	}
	
	@Test
	public void testResetPasswordHandleRequestViewWithErrors() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		PasswordResetData data = new PasswordResetData();
		data.setUid(uid);
		Mockito.when(bindingResult.hasErrors()).thenReturn(true);
		
		String result = controller.resetPassword(data, bindingResult);
		
		assertEquals("password-reset", result);
	}
	
	@Test
	public void testResetPasswordHandleRequestViewWithoutErrors() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		PasswordResetData data = new PasswordResetData();
		data.setUid(uid);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		PlatformUser user = new PlatformUser();
		user.setPasswordResetRequest(new PasswordResetRequest());
		Mockito.when(userService.userWithPasswordResetRequest(uid)).thenReturn(user);
		
		String result = controller.resetPassword(data, bindingResult);
		
		assertEquals("redirect:/login", result);
	}
	
	@Test
	public void testResetPasswordSavesNewPassword() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		PasswordResetData data = new PasswordResetData();
		data.setUid(uid);
		data.setPassword("password");
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		PlatformUser user = new PlatformUser();
		user.setPasswordResetRequest(new PasswordResetRequest());
		Mockito.when(userService.userWithPasswordResetRequest(uid)).thenReturn(user);
		
		controller.resetPassword(data, bindingResult);
		
		assertEquals("password", user.getPassword());
		Mockito.verify(userService).saveUser(user);
	}
	
	@Test
	public void testResetPasswordRejectsOldRequests() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		PasswordResetData data = new PasswordResetData();
		data.setUid(uid);
		data.setPassword("password");
		Mockito.when(bindingResult.hasErrors()).thenReturn(true);
		PlatformUser user = new PlatformUser();
		PasswordResetRequest request = new PasswordResetRequest();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		request.setGenerationDate(calendar.getTime());
		user.setPasswordResetRequest(request);
		Mockito.when(userService.userWithPasswordResetRequest(uid)).thenReturn(user);
		
		String result = controller.resetPassword(data, bindingResult);
		
		Mockito.verify(userService, Mockito.never()).saveUser(user);
		assertEquals("password-reset", result);
	}
	
}
