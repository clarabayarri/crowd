package com.crowdplatform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.util.DataMiner;

@Controller
public class GraphDataController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BatchExecutionService batchService;
	
	@Autowired
	private PlatformUserService userService;
	
	@Autowired
	private DataMiner dataMiner;
	
	@RequestMapping("/project/{projectId}/graphs")
	public String showGraphs(Model model, @PathVariable("projectId") String projectId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			model.addAttribute("project", project);
		}
		return "graphs";
	}
	
	@RequestMapping("/project/{projectId}/batch/{batchId}/graphs")
	public String showBatchGraphs(Model model, @PathVariable("projectId") String projectId,
			@PathVariable("batchId") Integer batchId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			if (batch != null) {
				model.addAttribute("batch", batch);
				model.addAttribute("project", project);
			}
		}
		return "graphs";
	}
	
	@RequestMapping("/project/{projectId}/data/field/{fieldName}")
	public @ResponseBody Map<Object, Object> obtainFieldData(@PathVariable("projectId") String projectId,
			@PathVariable("fieldName") String fieldName) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			return dataMiner.aggregateByField(project, fieldName);
		}
		return null;
	}
	
	@RequestMapping("/project/{projectId}/batch/{batchId}/data/field/{fieldName}")
	public @ResponseBody Map<Object, Object> obtainBatchFieldData(@PathVariable("projectId") String projectId,
			@PathVariable("batchId") Integer batchId,
			@PathVariable("fieldName") String fieldName) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			if (batch != null) {
				return dataMiner.aggregateByField(project, batch, fieldName);
			}
		}
		return null;
	}
}
