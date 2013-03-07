package com.crowdplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crowdplatform.service.ProjectService;

@Controller
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@RequestMapping(value={"/", "/projects"})
	public String listProjects(Model model) {
		model.addAttribute(projectService.listProjects());
		return "projects";
	}
	
	@RequestMapping("/project/{projectId}")
	public String getProject(@PathVariable("projectId") Integer projectId, Model model) {
		model.addAttribute(projectService.getProject(projectId));
		return "project";
	}
}
