package com.crowdplatform.service;

import com.crowdplatform.model.Batch;

public interface BatchService {

	/**
     * Retrieve all executions associated to a batch's tasks.
     * @param id The batch id
     * @return List with all the tasks from the batch identified by id with their executions loaded
     */
    public Batch getBatchWithTasksWithExecutions(Integer id);
}
