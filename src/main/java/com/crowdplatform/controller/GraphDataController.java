package com.crowdplatform.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Maps;

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
		if (field.getType().equals(Field.Type.MULTIVALUATE_STRING)) return getFieldMultivaluateStringCountData(project, fieldName);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Object, Object> getFieldMultivaluateStringCountData(Project project, String fieldName) {
		Map<Object, Object> result = Maps.newHashMap();
		for (Batch batch : project.getBatches()) {
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			for (Execution execution : collection.getExecutions()) {
				List<String> value = (List<String>) execution.getContents().get(fieldName);
				for (String item : value) {
					Integer count = (Integer) result.get(item);
					if (count == null) count = 0;
					count ++;
					result.put(item, count);
				}
			}
		}
		return result;
	}
}
