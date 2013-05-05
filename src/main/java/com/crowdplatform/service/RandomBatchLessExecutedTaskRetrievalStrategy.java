package com.crowdplatform.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.Task;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

@Service
public class RandomBatchLessExecutedTaskRetrievalStrategy implements TaskRetrievalStrategy {

	@Autowired
	private ProjectService projectService;
	
	@Override
	public List<Task> retrieveTasksForExecution(String projectId, Integer number) {
		Batch batch = getRandomBatch(projectId);
		return getLessExecutedTasksForBatch(batch, number);
	}
	
	@VisibleForTesting
	public Batch getRandomBatch(String projectId) {
		Project project = projectService.getProject(projectId);
		List<Batch> batches = project.getRunningBatches();
		if (batches.isEmpty()) {
			batches = project.getCompletedBatches();
		}
		int index = (new Random()).nextInt(batches.size());
		return batches.get(index);
	}

	@VisibleForTesting
	public List<Task> getLessExecutedTasksForBatch(Batch batch, Integer number) {
		List<Task> tasks = Lists.newArrayList();
		tasks.addAll(batch.getTasks());
		Collections.sort(tasks, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				Integer n1 = t1.getNumExecutions();
				Integer n2 = t2.getNumExecutions();
				return n1.compareTo(n2);
			}
		});
		return tasks.subList(0, Math.min(number, tasks.size()));
	}
}
