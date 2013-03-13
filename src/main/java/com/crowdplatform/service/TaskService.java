package com.crowdplatform.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Task;

public interface TaskService {

	public void addTask(Task task);
	
	public Task getTask(Integer id);
	
	public List<Task> listTasks();
	
	public void removeTask(Integer id);
	
	public void saveTask(Task task);
	
	public void createTasks(Batch batch, Set<Field> fields, List<Map<String, String>> fileContents);
	
}
