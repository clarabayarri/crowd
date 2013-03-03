package com.crowdplatform.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.Task;
import com.crowdplatform.service.BatchService;
import com.crowdplatform.service.ExecutionService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.TaskService;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/sample")
public class SampleDataController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
    private BatchService batchService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ExecutionService executionService;
	
	private Random random = new Random();
	
	@RequestMapping("/")
	public String createSampleData(Map<String, Object> map) {
		createSampleProject();
		
		return "redirect:/projects";
	}
	
	@RequestMapping("/clean")
	public String cleanSampleData() {
		List<Project> projects = projectService.listProjects();
		for (Project project : projects) {
			project.setBatches(null);
			projectService.saveProject(project);
		}
		List<Batch> batches = batchService.listBatches();
		for (Batch batch : batches) {
			batch.setTasks(null);
			batchService.saveBatch(batch);
		}
		List<Task> tasks = taskService.listTasks();
		for (Task task : tasks) {
			task.setExecutions(null);
			taskService.saveTask(task);
		}
		List<Execution> executions = executionService.listExecutions();
		for (Execution execution : executions) {
			executionService.removeExecution(execution.getId());
		}
		for (Task task : tasks) {
			taskService.removeTask(task.getId());
		}
		for (Batch batch : batches) {
			batchService.removeBatch(batch.getId());
		}
		for (Project project : projects) {
			projectService.removeProject(project.getId());
		}
		return "redirect:/projects";
	}
	
	private static final String[] definitions = {"{\"type\":\"insertion\",\"word\":\"nadie\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"e\", \"r\", \"y\", \"g\"]}",
		"{\"type\":\"insertion\",\"word\":\"tarde\", \"startIndex\":2, \"endIndex\":2, \"answers\":[\"s\", \"n\", \"i\", \"l\"]}",
		"{\"type\":\"insertion\",\"word\":\"estar\", \"startIndex\":1, \"endIndex\":1, \"answers\":[\"r\", \"n\", \"m\", \"i\", \"a\"]}",
		"{\"type\":\"insertion\",\"word\":\"padre\", \"startIndex\":2, \"endIndex\":2, \"answers\":[\"b\", \"p\", \"g\", \"t\"]}",
		"{\"type\":\"insertion\",\"word\":\"donde\", \"startIndex\":2, \"endIndex\":2, \"answers\":[\"r\", \"m\", \"s\", \"b\", \"g\"]}",
		"{\"type\":\"omission\",\"word\":\"donde\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"r\"]}",
		"{\"type\":\"omission\",\"word\":\"madre\", \"startIndex\":1, \"endIndex\":1, \"answers\":[\"s\"]}",
		"{\"type\":\"substitution\",\"word\":\"donde\", \"startIndex\":2, \"endIndex\":2, \"answers\":[\"r\", \"m\", \"s\", \"b\", \"g\"]}",
		"{\"type\":\"substitution\",\"word\":\"madre\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"s\", \"n\", \"i\", \"o\"]}",
		"{\"type\":\"derivation\",\"word\":\"felicidad\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"cion\", \"dero\", \"dor\", \"izar\", \"ura\"]}",
		"{\"type\":\"derivation\",\"word\":\"amoroso\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"ario\", \"able\", \"erio\", \"eto\", \"al\"]}",
		"{\"type\":\"separation\",\"word\":\"casa azul\", \"startIndex\":0, \"endIndex\":0, \"answers\":[]}",
		"{\"type\":\"separation\",\"word\":\"cosa buena\", \"startIndex\":0, \"endIndex\":0, \"answers\":[]}"};
	private static final Batch.State[] states = {Batch.State.RUNNING, Batch.State.PAUSED};
	
	private void createSampleProject() {
		Project project = new Project();
		project.setName("Awesome project");
		projectService.addProject(project);
		
		Set<Batch> batches = Sets.newHashSet();
		for (int i = 0; i < 5; ++i)
			batches.add(createSampleBatch(project));
		project.setBatches(batches);
		projectService.saveProject(project);
	}
	
	private Batch createSampleBatch(Project project) {
		Batch batch = new Batch();
		
		batch.setName("Wonderful" + random.nextInt(99));
		int exPerTask = random.nextInt(10) + 1;
		batch.setExecutionsPerTask(exPerTask);
		int stateIndex = random.nextInt(states.length);
		batch.setState(states[stateIndex]);
		batch.setProject(project);
		
		batchService.addBatch(batch);
		
		Random random = new Random();
		
		Set<Task> tasks = Sets.newHashSet();
		int numTasks = random.nextInt(15) + 1;
		for (int i = 0; i < numTasks; ++i) {
			Task task = new Task();
			int numExecutions = random.nextInt(exPerTask);
			task.setNumExecutions(numExecutions);
			task.setBatch(batch);
			int index = random.nextInt(definitions.length);
			task.setContents(definitions[index]);
			taskService.addTask(task);
			
			Set<Execution> executions = Sets.newHashSet();
			for (int j = 0; j < numExecutions; ++ j) {
				Execution execution = new Execution("blabla", task);
				executionService.addExecution(execution);
				executions.add(execution);
			}
			task.setExecutions(executions);
			taskService.saveTask(task);
			tasks.add(task);
		}
		batch.setTasks(tasks);
		batchService.saveBatch(batch);
		
		return batch;
	}
}