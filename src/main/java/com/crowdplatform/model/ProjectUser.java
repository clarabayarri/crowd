package com.crowdplatform.model;

import java.util.Map;

import com.crowdplatform.aux.ProjectUserInfo;
import com.google.common.collect.Maps;


public class ProjectUser {

	private Integer id;
	
	private Map<String, Object> contents;
	
	private Map<String, Number> userDataContents;
	
	public ProjectUser() {
		contents = Maps.newHashMap();
	}
	
	public ProjectUser(ProjectUserInfo info) {
		this.contents = info.getContents();
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
	
	public void addContents(String key, Object value) {
		contents.put(key, value);
	}

	public Map<String, Number> getUserDataContents() {
		return userDataContents;
	}

	public void setUserDataContents(Map<String, Number> userDataContents) {
		this.userDataContents = userDataContents;
	}

	public void addUserDataContents(String key, Number value) {
		this.userDataContents.put(key, value);
	}
}
