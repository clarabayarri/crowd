package com.example.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.model.Batch;
import com.example.model.Task;
import com.example.service.BatchService;
import com.example.service.TaskService;
import com.google.common.collect.Sets;

@Controller
@RequestMapping("/sample")
public class SampleDataController {

	@Autowired
    private BatchService batchService;
	
	@Autowired
	private TaskService taskService;
	
	private Random random = new Random();
	
	@RequestMapping("/")
	public String createSampleData(Map<String, Object> map) {
		for (int i = 0; i < 5; ++i)
			createSampleBatch();
		
		return "redirect:/batches/";
	}
	
	@RequestMapping("/clean")
	public String cleanSampleData() {
		List<Task> tasks = taskService.listTasks();
		for (Task task : tasks) {
			taskService.removeTask(task.getId());
		}
		List<Batch> batches = batchService.listBatches();
		for (Batch batch : batches) {
			batchService.removeBatch(batch.getId());
		}
		return "redirect:/batches/";
	}
	
	private static final String[] definitions = {"{\"type\":\"insertion\",\"word\":\"palabra\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"e\", \"b\", \"r\", \"o\"]}",
		"{\"type\":\"insertion\",\"word\":\"cometa\", \"startIndex\":3, \"endIndex\":3, \"answers\":[\"i\", \"b\", \"r\", \"o\"]}",
		"{\"type\":\"insertion\",\"word\":\"singular\", \"startIndex\":1, \"endIndex\":1, \"answers\":[\"a\", \"e\"]}",
		"{\"type\":\"insertion\",\"word\":\"pediatria\", \"startIndex\":7, \"endIndex\":7, \"answers\":[\"e\", \"b\", \"r\", \"o\", \"a\", \"u\"]}",
		"{\"type\":\"insertion\",\"word\":\"ejercicio\", \"startIndex\":8, \"endIndex\":8, \"answers\":[\"a\", \"b\", \"e\", \"i\", \"d\"]}"};
	
	private void createSampleBatch() {
		Batch batch = new Batch();
		
		batch.setName("Wonderful" + random.nextInt(99));
		int exPerTask = random.nextInt(10) + 1;
		batch.setExecutionsPerTask(exPerTask);
		
		batchService.addBatch(batch);
		
		Random random = new Random();
		
		Set<Task> tasks = Sets.newHashSet();
		int numTasks = random.nextInt(15) + 1;
		for (int i = 0; i < numTasks; ++i) {
			Task task = new Task();
			task.setNumExecutions(random.nextInt(exPerTask));
			task.setBatch(batch);
			int index = random.nextInt(definitions.length);
			task.setContents(definitions[index]);
			taskService.addTask(task);
			tasks.add(task);
		}
		batch.setTasks(tasks);
		
		
	}
}
