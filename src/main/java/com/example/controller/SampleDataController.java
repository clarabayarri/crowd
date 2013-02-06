package com.example.controller;

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
public class SampleDataController {

	@Autowired
    private BatchService batchService;
	
	@Autowired
	private TaskService taskService;
	
	private Random random = new Random();
	
	@RequestMapping("/sample/")
	public String createSampleData(Map<String, Object> map) {
		for (int i = 0; i < 5; ++i)
			createSampleBatch();
		
		return "redirect:/batches/";
	}
	
	private void createSampleBatch() {
		Batch batch = new Batch();
		
		batch.setName("Wonderful" + random.nextInt(99));
		int exPerTask = random.nextInt(10) + 1;
		batch.setExecutionsPerTask(exPerTask);
		
		batchService.addBatch(batch);
		
		Set<Task> tasks = Sets.newHashSet();
		int numTasks = random.nextInt(15) + 1;
		for (int i = 0; i < numTasks; ++i) {
			Task task = new Task();
			task.setNumExecutions(random.nextInt(exPerTask));
			task.setBatch(batch);
			taskService.addTask(task);
			tasks.add(task);
		}
		batch.setTasks(tasks);
		
		
	}
}
