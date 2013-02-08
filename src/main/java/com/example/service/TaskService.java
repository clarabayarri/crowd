package com.example.service;

import java.util.List;

import com.example.model.Task;

public interface TaskService {

	public void addTask(Task task);
	
	public Task getTask();
	
	public List<Task> listTasks();
	
	public void removeTask(Integer id);
	
}
