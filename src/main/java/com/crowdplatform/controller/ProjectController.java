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
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;

@Controller
public class ProjectController {

	@Autowired
	private PlatformUserService userService;
	
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
		if (userService.currentUserIsAuthorizedForProject(projectId)) {
	    	Project project = projectService.getProject(projectId);
			model.addAttribute(project);
	    }
		return "project";
	}
	
	@RequestMapping("/project/{projectId}/delete")
	public String deleteProject(@PathVariable Integer projectId) {
		if (userService.currentUserIsAuthorizedForProject(projectId)) {
			projectService.removeProject(projectId);
		}
		return "redirect:/projects";
	}
}
