package com.crowdplatform.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.TaskService;
import com.crowdplatform.util.FileReader;
import com.crowdplatform.util.FileWriter;
import com.crowdplatform.util.GoogleFusiontablesAdapter;
import com.google.common.collect.Lists;

@Controller
public class BatchController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private BatchService batchService;
	
	@Autowired
	private PlatformUserService userService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private GoogleFusiontablesAdapter dataExporter;

	@RequestMapping("/project/{projectId}/batch/{batchId}")
	public String getBatch(@PathVariable("projectId") Long projectId, 
			@PathVariable("batchId") Integer batchId, Model model,
			@RequestParam(value="created", required=false) Boolean created,
			@RequestParam(value="export-error", required=false) Boolean exportError) {
		if (userService.currentUserIsAuthorizedForBatch(projectId, batchId)) {
			model.addAttribute(projectService.getProject(projectId));
			model.addAttribute(batchService.getBatch(batchId));
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
	public String startBatch(@PathVariable("projectId") Long projectId, 
			@PathVariable("batchId") Integer batchId) {
		if (userService.currentUserIsAuthorizedForBatch(projectId, batchId)) {
			batchService.startBatch(batchId);
		}
		return "redirect:/project/" + projectId + "/batch/" + batchId;
	}

	@RequestMapping("/project/{projectId}/batch/{batchId}/pause")
	public String pauseBatch(@PathVariable("projectId") Long projectId, 
			@PathVariable("batchId") Integer batchId) {
		if (userService.currentUserIsAuthorizedForBatch(projectId, batchId)) {
			batchService.pauseBatch(batchId);
		}
		return "redirect:/project/" + projectId + "/batch/" + batchId;
	}

	@RequestMapping("/project/{projectId}/batch/{batchId}/delete")
	public String deleteBatch(@PathVariable("projectId") Long projectId,
			@PathVariable("batchId") Integer batchId) {
		if (userService.currentUserIsAuthorizedForBatch(projectId, batchId)) {
			Project project = projectService.getProject(projectId);
			project.removeBatch(batchId);
			projectService.saveProject(project);
			batchService.removeBatch(batchId);
		}
		return "redirect:/project/" + projectId;
	}

	@RequestMapping(value={"/project/{projectId}/batch/create"}, method=RequestMethod.GET)
	public String newBatch(@PathVariable("projectId") Long projectId, Model model) {
		if (userService.currentUserIsAuthorizedForProject(projectId)) {
			Project project = projectService.getProject(projectId);
			model.addAttribute(project);
	    }
		Batch batch = new Batch();
		model.addAttribute(batch);
		return "create";
	}

	@RequestMapping(value="/project/{projectId}/batch/create", method = RequestMethod.POST)
	public String createBatch(@Valid Batch batch, @PathVariable("projectId") Long projectId, 
			BindingResult bindingResult, 
			@RequestParam(value="taskFile", required=false) MultipartFile taskFile) {
		if (bindingResult.hasErrors()) {
			return "create";
		}

		if (taskFile != null && !taskFile.isEmpty() && !validateFileFormat(taskFile)) {
			bindingResult.reject("error.file.format");
			return "create";
		}

		if (userService.currentUserIsAuthorizedForProject(projectId)) {
			batchService.addBatch(batch);
			Project project = projectService.getProject(projectId);
			project.addBatch(batch);
			
			if (taskFile != null && !taskFile.isEmpty()) {
				Set<Field> fields = project.getInputFields();
				FileReader reader = new FileReader();
				try {
					List<Map<String, String>> fileContents = reader.readCSVFile(taskFile);
					taskService.createTasks(batch, fields, fileContents);
				} catch (IOException e) {
					bindingResult.reject("error.file.contents");
					return "create";
				}
			}
			projectService.saveProject(project);
		}

		return "redirect:/project/" + projectId + "/batch/" + batch.getId() + "?created=true";
	}

	private boolean validateFileFormat(MultipartFile file) {
		if (!file.getContentType().equals("text/csv")) return false;
		return true;
	}



	@RequestMapping("/project/{projectId}/batch/{batchId}/download")
	public void downloadBatch(@PathVariable("projectId") Long projectId, 
			@PathVariable("batchId") Integer batchId, HttpServletResponse response) {
		Batch batch = batchService.getBatchWithTasksWithExecutions(batchId);
		Project project = projectService.getProject(projectId);
		try {
			String writer = (new FileWriter()).writeTasksExecutions(Lists.newArrayList(batch.getOrderedTasks()), 
					project.getOrderedInputFields(), project.getOrderedOutputFields(), true);
			response.getWriter().write(writer);
			response.setContentType("text/csv");
			response.setHeader("Content-Disposition","attachment; filename=batch-executions-" + batchId + ".csv");
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}
	
	@RequestMapping("/project/{projectId}/batch/{batchId}/export")
	public String exportBatch(@PathVariable("projectId") Long projectId, 
			@PathVariable("batchId") Integer batchId) {
		Project project = projectService.getProject(projectId);
		Batch batch = batchService.getBatchWithTasksWithExecutions(batchId);
		String url = dataExporter.exportDataURL(project, batch);
		batchService.saveBatch(batch);
		if (url != null) {
			return "redirect:" + url;
		}
		return "redirect:/project/" + projectId + "/batch/" + batchId + "?export-error=true";
	}
}
