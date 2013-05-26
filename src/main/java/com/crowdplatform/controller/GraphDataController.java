package com.crowdplatform.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.util.FileWriter;

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
}
