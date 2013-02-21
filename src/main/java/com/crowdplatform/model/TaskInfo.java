package com.crowdplatform.model;


public class TaskInfo {

	private Integer id;
	
	private String contents;
	
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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
