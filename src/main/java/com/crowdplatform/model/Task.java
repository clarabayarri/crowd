package com.crowdplatform.model;

import java.util.Map;

import com.google.common.collect.Maps;

public class Task {

    private Integer taskId;
    
    private Integer batchId;
	
	private Map<String, Object> contents;
	
	private Integer numExecutions;
	
	public Task() {
		contents = Maps.newHashMap();
		this.numExecutions = 0;
	}
	
	public Integer getId() {
		return taskId;
	}

	public void setTaskId(Integer id) {
		this.taskId = id;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}
	
	public void addContents(String key, Object value) {
		contents.put(key, value);
	}
	
	public int getNumExecutions() {
		return numExecutions;
	}

	public void setNumExecutions(Integer numExecutions) {
		this.numExecutions = numExecutions;
	}
}
