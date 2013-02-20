package com.example.service;

import java.util.List;

import com.example.model.Task;

public interface TaskService {

	public void addTask(Task task);
	
	public Task getRandomTask();
	
	public Task getTask(Integer id);
	
	public List<Task> listTasks();
	
	public void removeTask(Integer id);
	
	public void saveTask(Task task);
	
}
