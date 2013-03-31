package com.crowdplatform.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
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
	 * @param id The task id
	 * @return Task corresponding to id
	 */
	public Task getTask(Integer id);
	
	/**
	 * Create a set of tasks and add them to the provided Batch.
	 * @param batch Batch that will contain the tasks
	 * @param fields Fields that should be considered for the Task contents
	 * @param fileContents Data used to create the tasks
	 */
	public void createTasks(Batch batch, Set<Field> fields, List<Map<String, String>> fileContents);
	
}
