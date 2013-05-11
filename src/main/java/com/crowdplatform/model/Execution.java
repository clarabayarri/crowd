package com.crowdplatform.model;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

public class Execution {

	private Integer id;
	
	private Date date;
	
	private Map<String, Object> contents;
	
	private Integer projectUserId;
	
	private Integer taskId;
	
	public Execution() {
		this.date = new Date();
		this.contents = Maps.newHashMap();
	}
	
	public Execution(Map<String, Object> contents) {
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

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}
	
	public void addContents(String key, Object value) {
		contents.put(key, value);
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
