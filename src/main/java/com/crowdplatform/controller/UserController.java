package com.crowdplatform.controller;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.crowdplatform.model.PasswordResetData;
import com.crowdplatform.model.PasswordResetRequest;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Registration;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.util.MailSender;
import com.crowdplatform.util.PasswordResetDataValidator;
import com.crowdplatform.util.RegistrationValidator;

@Controller
public class UserController {

	@Autowired
	private RegistrationValidator registrationValidation;

	@Autowired
	private PasswordResetDataValidator passwordResetValidator;

	@Autowired @Qualifier("org.springframework.security.authenticationManager")
	protected AuthenticationManager authenticationManager;

	@Autowired
	private PlatformUserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private MailSender mailSender;

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public MailSender getMailSender() {
		if (this.mailSender == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext("mail.xml");
			this.mailSender = (MailSender) context.getBean("mailMail");
		}
		return this.mailSender;
	}

	@RequestMapping("/login")
	public String loadLogin(Model model, @RequestParam(value="error", required=false) Boolean error) {
		if (error != null) {
			model.addAttribute("error", error);
		}
		return "login";
	}

	@RequestMapping(value={"/register"}, method=RequestMethod.GET)
	public String showRegistration(Model model) {
		model.addAttribute(new Registration());
		return "register";
	}

	@RequestMapping(value={"/register"}, method=RequestMethod.POST)
	public String processRegistration(@Valid Registration registration, 
			BindingResult bindingResult, HttpServletRequest request) {
		registrationValidation.validate(registration, bindingResult);
		if (bindingResult.hasErrors()) {
			return "register";
		}
		PlatformUser newUser = new PlatformUser(registration);
		newUser.setPassword(passwordEncoder.encodePassword(registration.getPassword(), null));
		userService.addUser(newUser);
		PlatformUser user = userService.getUser(registration.getUsername());
		if (user != null) {
			authenticateUserAndSetSession(registration.getUsername(), registration.getPassword(), request);
			return "redirect:/projects?registered=true";
		}
		bindingResult.reject("registration.error");
		return "register";
	}

	@RequestMapping(value={"/forgot"}, method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void forgotPassword(String username) {
		PlatformUser user = userService.getUserByUsernameOrEmail(username);
		if (user != null) {
			PasswordResetRequest request = new PasswordResetRequest();
			SecureRandom secureRandom = new SecureRandom();
			request.setId(secureRandom.nextLong());
			user.setPasswordResetRequest(request);

			userService.saveUser(user);

			getMailSender().sendPasswordResetMail(user, request.getId());
		} else {
			System.out.println("UserController: Forgot password attempted with unexisting user '" + username + "'");
		}
	}

	@RequestMapping("/forgot/{uid}")
	public String loadPasswordReset(@PathVariable("uid") Long uid, Model model) {
		PasswordResetData data = new PasswordResetData();
		model.addAttribute(data);
		model.addAttribute("uid", uid);
		return "password-reset";
	}

	@RequestMapping("/reset")
	public String resetPassword(@Valid PasswordResetData data, BindingResult bindingResult) {
		passwordResetValidator.validate(data, bindingResult);

		PlatformUser user = userService.userWithPasswordResetRequest(data.uid);
		if (user == null) {
			bindingResult.reject("password.change.error");
			System.out.println("UserController: Password reset attempted with unexisting request '" + data.getUid() + "' or wrong date.");
		}
		
		if (user != null && user.getPasswordResetRequest().getGenerationDate().before(getDateForResetLimit())) {
			bindingResult.reject("password.change.error");
			System.out.println("UserController: Password reset attempted for user '" + user.getUsername() + "' wrong date.");
		}

		if (!bindingResult.hasErrors()) {
			user.setPassword(passwordEncoder.encodePassword(data.getPassword(), null));
			user.setPasswordResetRequest(null);
			userService.saveUser(user);

			// TODO: send email alerting the password was changed

			return "redirect:/login";
		}

		return "password-reset";
	}
	
	private Date getDateForResetLimit() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -14);
		return calendar.getTime();
	}

	private void authenticateUserAndSetSession(String username, String password, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(username, password);

		request.getSession();

		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authenticatedUser = authenticationManager.authenticate(token);

		SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	}
}
