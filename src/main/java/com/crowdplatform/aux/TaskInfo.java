package com.crowdplatform.aux;

import java.util.Map;

import com.crowdplatform.model.Task;


public class TaskInfo {

private Integer batchId;
	
	private Integer taskId;
	
	private Map<String, Object> contents;
	
	public TaskInfo(Task task) {
		this.batchId = task.getBatchId();
		this.taskId = task.getId();
		this.contents = task.getContents();
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
}
