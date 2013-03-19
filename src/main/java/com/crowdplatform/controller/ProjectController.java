package com.crowdplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crowdplatform.model.User;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping("/projects")
	public String listProjects(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	String username = auth.getName();
		    User user = userService.getUser(username);
			model.addAttribute(user.getOrderedProjects());
	    }
		return "projects";
	}
	
	@RequestMapping("/project/{projectId}")
	public String getProject(@PathVariable("projectId") Integer projectId, Model model) {
		model.addAttribute(projectService.getProject(projectId));
		return "project";
	}
}
