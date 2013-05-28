package com.crowdplatform.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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
import com.crowdplatform.util.FileWriter;
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
	
	@RequestMapping("/project/{projectId}/data/executions")
	public void downloadData(@PathVariable("projectId") String projectId, 
			HttpServletResponse response) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			try {
				String writer = writeDatesExecutions(project);
				response.getWriter().write(writer);
				response.setContentType("text/csv");
				response.setHeader("Content-Disposition","attachment; filename=batch-executions-data.csv");
				response.flushBuffer();
			} catch (IOException ex) {
				throw new RuntimeException("IOError writing file to output stream");
			}
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " download batch.");
	    }
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
	
	private String writeDatesExecutions(Project project) throws IOException {
		FileWriter writer = new FileWriter();
		boolean first = true;
		String result = "";
		for (Batch batch : project.getBatches()) {
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			String partialResult = writer.writeTasksExecutions(project, batch, collection, first);
			first = false;
			result += partialResult;
		}
		return result;
	}
	
	private Map<Object, Object> getFieldData(Project project, String fieldName) {
		if (fieldName.equals("date")) return getFieldDateCountData(project);
		Field field = project.getField(fieldName);
		if (field.getType().equals(Field.Type.INTEGER)) return getFieldIntegerCoundData(project, fieldName);
		if (field.getType().equals(Field.Type.STRING)) return getFieldStringCountData(project, fieldName);
		if (field.getType().equals(Field.Type.MULTIVALUATE_STRING)) return getFieldMultivaluateStringCountData(project, fieldName);
		return null;
	}
	
	private Map<Object, Object> getFieldDateCountData(Project project) {
		Map<Object, Object> result = Maps.newHashMap();
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (Batch batch : project.getBatches()) {
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			for (Execution execution : collection.getExecutions()) {
				String value = myFormat.format(execution.getDate());
				Integer count = (Integer) result.get(value);
				if (count == null) count = 0;
				count ++;
				result.put(value, count);
			}
		}
		return result;
	}
	
	private Map<Object, Object> getFieldStringCountData(Project project, String fieldName) {
		Map<Object, Object> result = Maps.newHashMap();
		for (Batch batch : project.getBatches()) {
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			for (Execution execution : collection.getExecutions()) {
				String value = (String) execution.getContents().get(fieldName);
				System.out.println(value);
				if (value != null) {
					Integer count = (Integer) result.get(value);
					if (count == null) count = 0;
					count ++;
					result.put(value, count);
				}
			}
		}
		return result;
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
	
	private Map<Object, Object> getFieldIntegerCoundData(Project project, String fieldName) {
		Map<Integer, Integer> result = Maps.newHashMap();
		for (Batch batch : project.getBatches()) {
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			for (Execution execution : collection.getExecutions()) {
				Integer value = ((Number) execution.getContents().get(fieldName)).intValue();
				Integer count = (Integer) result.get(value);
				if (count == null) count = 0;
				count ++;
				result.put(value, count);
			}
		}
		Integer minValue = Collections.min(result.keySet());
		Integer maxValue = Collections.max(result.keySet());
		Integer step = Math.max(1, (int) Math.ceil((maxValue - minValue) / 18.0));
		Map<Object, Object> ordered = Maps.newLinkedHashMap();
		for (int i = minValue-step; i <= maxValue + step; i += step) {
			ordered.put(i, 0);
		}
		for (Integer key : result.keySet()) {
			Integer slot = key - (key%step);
			Integer count = (Integer) ordered.get(slot);
			count += result.get(key);
			ordered.put(slot, count);
		}
		return ordered;
	}
}
