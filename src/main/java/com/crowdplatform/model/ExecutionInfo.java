package com.crowdplatform.model;

import java.util.Map;

import com.google.common.collect.Maps;


public class ExecutionInfo {

	private Integer batchId;
	
	private Integer taskId;
	
	private Map<String, Object> contents;
	
	private Integer userId;
	
	public ExecutionInfo() {
		contents = Maps.newHashMap();
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
