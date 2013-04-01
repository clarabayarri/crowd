package com.crowdplatform.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Task;
import com.google.common.collect.Lists;

@Service
public class RandomBatchLessExecutedTaskRetrievalStrategy implements TaskRetrievalStrategy {

	@Autowired
	private BatchService batchService;
	
	@Override
	public List<Task> retrieveTasksForExecution(Integer number) {
		Batch batch = getRandomBatch();
		return getLessExecutedTasksForBatch(batch, number);
	}
	
	private Batch getRandomBatch() {
		List<Integer> batches = batchService.listRunningBatchIds();
		int total = batches.size();
		int index = (new Random()).nextInt(total);
		return batchService.getBatch(batches.get(index));
	}

	private List<Task> getLessExecutedTasksForBatch(Batch batch, Integer number) {
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
