package com.crowdplatform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crowdplatform.model.Registration;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.service.UserService;
import com.crowdplatform.util.RegistrationValidator;

@Controller
public class UserController {

	@Autowired
    private RegistrationValidator registrationValidation;
	
	@Autowired @Qualifier("org.springframework.security.authenticationManager")
    protected AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

    public void setRegistrationValidation(
    		RegistrationValidator registrationValidation) {
            this.registrationValidation = registrationValidation;
    }
    
	@RequestMapping("/login")
	public String loadLogin() {
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
		userService.addUser(new PlatformUser(registration));
		PlatformUser user = userService.getUser(registration.getUsername());
		if (user != null) {
			authenticateUserAndSetSession(user, request);
			return "redirect:/projects";
		}
		bindingResult.reject("registration.error");
		return "register";
	}
	
	private void authenticateUserAndSetSession(PlatformUser user, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

	    request.getSession();

	    token.setDetails(new WebAuthenticationDetails(request));
	    Authentication authenticatedUser = authenticationManager.authenticate(token);

	    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
	}
}
