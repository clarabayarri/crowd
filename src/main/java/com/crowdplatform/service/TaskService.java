package com.crowdplatform.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Task;

public interface TaskService {

	public void addTask(Task task);
	
	public Task getRandomTask();
	
	public Task getTask(Integer id);
	
	public List<Task> listTasks();
	
	public void removeTask(Integer id);
	
	public void saveTask(Task task);
	
	public void createTasks(Batch batch, MultipartFile taskFile) throws IOException;
	
}
