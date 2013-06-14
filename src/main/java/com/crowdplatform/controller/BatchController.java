package com.crowdplatform.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.util.DataViewer;
import com.crowdplatform.util.FileReader;
import com.crowdplatform.util.FileWriter;
import com.crowdplatform.util.TaskCreator;

@Controller
public class BatchController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private BatchExecutionService batchService;
	
	@Autowired
	private PlatformUserService userService;
	
	@Autowired
	private DataViewer dataExporter;
	
	@Autowired
	private TaskCreator taskCreator;
	
	@Autowired
	private FileWriter fileWriter;
	
	@Autowired
	private FileReader fileReader;

	@RequestMapping("/project/{projectId}/batch/{batchId}")
	public String getBatch(@PathVariable("projectId") String projectId, 
			@PathVariable("batchId") Integer batchId, Model model,
			@RequestParam(value="created", required=false) Boolean created,
			@RequestParam(value="export-error", required=false) Boolean exportError) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			model.addAttribute(project);
			model.addAttribute(project.getBatch(batchId));
		} else {
	    	System.out.println("BatchController: Access denied to user " + user.getUsername() + " for project " + projectId + ".");
	    }
		if (created != null) {
			model.addAttribute("created", created);
		}
		if (exportError != null) {
			model.addAttribute("export-error", exportError);
		}
		return "batch";
	}

	@RequestMapping("/project/{projectId}/batch/{batchId}/start")
	public String startBatch(@PathVariable("projectId") String projectId, 
			@PathVariable("batchId") Integer batchId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			if (batch != null) {
				batch.setState(Batch.State.RUNNING);
				projectService.saveProject(project);
			}
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " start batch.");
	    }
		return "redirect:/project/" + projectId + "/batch/" + batchId;
	}

	@RequestMapping("/project/{projectId}/batch/{batchId}/pause")
	public String pauseBatch(@PathVariable("projectId") String projectId, 
			@PathVariable("batchId") Integer batchId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			if (batch != null) {
				batch.setState(Batch.State.PAUSED);
				projectService.saveProject(project);
			}
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " pause batch.");
	    }
		return "redirect:/project/" + projectId + "/batch/" + batchId;
	}

	@RequestMapping("/project/{projectId}/batch/{batchId}/delete")
	public String deleteBatch(@PathVariable("projectId") String projectId,
			@PathVariable("batchId") Integer batchId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			batchService.removeCollection(batch.getExecutionCollectionId());
			project.removeBatch(batchId);
			projectService.saveProject(project);
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " delete batch.");
	    }
		return "redirect:/project/" + projectId;
	}

	@RequestMapping(value={"/project/{projectId}/batch/create"}, method=RequestMethod.GET)
	public String newBatch(@PathVariable("projectId") String projectId, Model model) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			model.addAttribute(project);
	    } else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " new batch.");
	    }
		Batch batch = new Batch();
		model.addAttribute(batch);
		return "create";
	}

	@RequestMapping(value="/project/{projectId}/batch/create", method = RequestMethod.POST)
	public String createBatch(@Valid Batch batch, @PathVariable("projectId") String projectId, 
			BindingResult bindingResult, 
			@RequestParam(value="taskFile", required=false) MultipartFile taskFile) {
		if (bindingResult.hasErrors()) {
			return "create";
		}

		if (taskFile != null && !taskFile.isEmpty() && !validateFileFormat(taskFile)) {
			bindingResult.reject("error.file.format");
			return "create";
		}

		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			project.addBatch(batch);
			projectService.saveProject(project);
			BatchExecutionCollection collection = new BatchExecutionCollection();
			collection.setProjectId(project.getId());
			collection.setBatchId(batch.getId());
			batchService.saveExecutions(collection);
			batch.setExecutionCollectionId(collection.getId());
			
			if (taskFile != null && !taskFile.isEmpty()) {
				List<Field> fields = project.getInputFields();
				try {
					List<Map<String, String>> fileContents = fileReader.readCSVFile(taskFile);
					taskCreator.createTasks(batch, fields, fileContents);
				} catch (IOException e) {
					bindingResult.reject("error.file.contents");
					return "create";
				}
			}
			projectService.saveProject(project);
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " create batch.");
	    }

		return "redirect:/project/" + projectId + "/batch/" + batch.getId() + "?created=true";
	}

	private boolean validateFileFormat(MultipartFile file) {
		if (!file.getContentType().equals("text/csv")) return false;
		return true;
	}

	@RequestMapping("/project/{projectId}/batch/{batchId}/download")
	public void downloadBatch(@PathVariable("projectId") String projectId, 
			@PathVariable("batchId") Integer batchId, HttpServletResponse response) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			try {
				String writer = fileWriter.writeTasksExecutions(project, batch, collection, true);
				response.getWriter().write(writer);
				response.setContentType("text/csv");
				response.setHeader("Content-Disposition","attachment; filename=batch-executions-" + batch.getName().trim().replace(" ", "-") + ".csv");
				response.flushBuffer();
			} catch (IOException ex) {
				throw new RuntimeException("IOError writing file to output stream");
			}
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " download batch.");
	    }
	}
	
	@RequestMapping("/project/{projectId}/batch/{batchId}/export")
	public String viewBatchData(@PathVariable("projectId") String projectId, 
			@PathVariable("batchId") Integer batchId) {
		Project project = projectService.getProject(projectId);
		PlatformUser user = userService.getCurrentUser();
		if (project.getOwnerId().equals(user.getUsername())) {
			Batch batch = project.getBatch(batchId);
			BatchExecutionCollection collection = batchService.getExecutions(batch.getExecutionCollectionId());
			String url = dataExporter.getDataURL(project, batch, collection);
			projectService.saveProject(project);
			if (url != null) {
				return "redirect:" + url;
			}
		} else {
	    	System.out.println("ProjectController: Access denied to user " + user.getUsername() + " for project " + projectId + " export batch.");
	    }
		return "redirect:/project/" + projectId + "/batch/" + batchId + "?export-error=true";
	}
}
