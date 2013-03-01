package com.crowdplatform.service;

import com.crowdplatform.model.Task;

public interface TaskRetrievalStrategy {

	public Task retrieveTaskForExecution();
	
}
