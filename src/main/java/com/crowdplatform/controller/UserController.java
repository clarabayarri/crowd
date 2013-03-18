package com.crowdplatform.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.crowdplatform.model.Registration;
import com.crowdplatform.model.User;
import com.crowdplatform.service.UserService;
import com.crowdplatform.util.RegistrationValidator;

@Controller
public class UserController {

	@Autowired
    private RegistrationValidator registrationValidation;
	
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
	public String processRegistration(@Valid Registration registration, BindingResult bindingResult) {
		registrationValidation.validate(registration, bindingResult);
		if (bindingResult.hasErrors()) {
			return "register";
		}
		userService.addUser(new User(registration));
		return "register";
	}
}
