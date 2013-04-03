package com.crowdplatform.model;


public class ExecutionInfo {

	private Integer taskId;
	
	private String contents;
	
	private Integer userId;
	
	public ExecutionInfo() {
		
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
