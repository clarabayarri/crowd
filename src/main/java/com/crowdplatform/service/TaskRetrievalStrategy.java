package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Task;

public interface TaskRetrievalStrategy {

	public List<Task> retrieveTasksForExecution(Long projectId, Integer number);
	
}
