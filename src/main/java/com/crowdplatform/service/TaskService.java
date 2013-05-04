package com.crowdplatform.service;

import com.crowdplatform.model.Task;

public interface TaskService {
	
	/**
	 * Save an existing task.
	 * @param task
	 */
	public void saveTask(Task task);
	
	/**
	 * Remove an existing task.
	 * @param id The task id
	 */
	public void removeTask(Integer id);

	/**
	 * Retrieve a persisted task.
	 * @param projectId The Project id
	 * @param taskId The task id
	 * @return Task corresponding to id
	 */
	public Task getTask(String projectId, Integer taskId);
	
}
