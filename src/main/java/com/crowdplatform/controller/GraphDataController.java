package com.crowdplatform.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crowdplatform.model.Field;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;

@Controller
public class GraphDataController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BatchExecutionService batchService;
	
	@Autowired
	private PlatformUserService userService;
	
	@RequestMapping("/project/{projectId}/graphs")
	public String showGraphs(Model model, @PathVariable("projectId") String projectId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			model.addAttribute("project", project);
		}
		return "graphs";
	}
	
	@RequestMapping("/project/{projectId}/data/field/{fieldName}")
	public @ResponseBody Map<Object, Object> obtainFieldData(@PathVariable("projectId") String projectId,
			@PathVariable("fieldName") String fieldName) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			return getFieldData(project, fieldName);
		}
		return null;
	}
	
	private Map<Object, Object> getFieldData(Project project, String fieldName) {
		if (fieldName.equals("date")) return projectService.getAggregatedDataByDate(project);
		Field field = project.getField(fieldName);
		if (field.getType().equals(Field.Type.INTEGER)) return projectService.getAggregatedDataByFieldWithSteps(project, fieldName);
		if (field.getType().equals(Field.Type.STRING)) return projectService.getAggregatedDataByField(project, fieldName);
		if (field.getType().equals(Field.Type.MULTIVALUATE_STRING)) return projectService.getAggregatedDataByMultivaluateField(project, fieldName);
		return null;
	}
}
