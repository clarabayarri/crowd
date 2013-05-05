package com.crowdplatform.model;

import java.util.Date;

public class Execution {

	private Integer id;
	
	private Date date;
	
	private String contents;
	
	private Integer projectUserId;
	
	private Integer taskId;
	
	public Execution() {
		this.date = new Date();
	}
	
	public Execution(String contents) {
		this.contents = contents;
		this.date = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getProjectUserId() {
		return projectUserId;
	}

	public void setProjectUserId(Integer projectUserId) {
		this.projectUserId = projectUserId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
}
