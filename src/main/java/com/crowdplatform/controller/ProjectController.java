package com.crowdplatform.controller;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		PlatformUser user = userService.getCurrentUser();
	    if (user != null) {
	    	List<Project> projects = projectService.getProjectsForUser(user);
	    	model.addAttribute(projects);
	    }
	    if (registered != null) {
	    	model.addAttribute("registered", registered);
	    }
		return "projects";
	}
	
	@RequestMapping("/project/{projectId}")
	public String getProject(@PathVariable("projectId") String projectId, Model model) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			model.addAttribute(project);
	    } else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + ".");
	    }
		return "project";
	}
	
	@RequestMapping("/project/{projectId}/resetUID")
	public String resetProjectUID(@PathVariable("projectId") String projectId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			SecureRandom random = new SecureRandom();
			project.setUid(random.nextLong());
			projectService.saveProject(project);
	    } else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " reset UID.");
	    }
		return "redirect:/project/" + projectId;
	}
	
	@RequestMapping("/project/{projectId}/delete")
	public String deleteProject(@PathVariable String projectId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			projectService.removeProject(projectId);
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " delete project.");
	    }
		return "redirect:/projects";
	}
}
