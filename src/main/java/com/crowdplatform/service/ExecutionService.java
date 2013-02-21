package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Execution;

public interface ExecutionService {

	public void addExecution(Execution execution);
	
	public List<Execution> listExecutions();
	
	public void removeExecution(Integer id);
}
