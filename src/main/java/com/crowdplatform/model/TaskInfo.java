package com.crowdplatform.model;

import java.util.Map;


public class TaskInfo {

	private Integer id;
	
	private Map<String, Object> contents;
	
	public TaskInfo(Task task) {
		this.id = task.getId();
		this.contents = task.getContents();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}
}
