package com.crowdplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;
	
	@RequestMapping("/projects")
	public String listProjects(Model model, @RequestParam(value="registered", required=false) Boolean registered) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	String username = auth.getName();
		    PlatformUser user = userService.getUser(username);
			model.addAttribute(user.getOrderedProjects());
	    }
	    if (registered != null) {
	    	model.addAttribute("registered", registered);
	    }
		return "projects";
	}
	
	@RequestMapping("/project/{projectId}")
	public String getProject(@PathVariable("projectId") Integer projectId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	String username = auth.getName();
		    PlatformUser user = userService.getUser(username);
			Project project = projectService.getProject(projectId);
			for (Project p : user.getProjects()) {
				if (p.getId().equals(projectId)) {
					model.addAttribute(project);
				}
			}
	    }
		return "project";
	}
}
