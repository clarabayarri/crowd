package com.crowdplatform.model;

import java.util.Map;

public class ProjectUserInfo {

	private Long projectUid;
	
	private Map<String, Object> contents;

	public Long getProjectUid() {
		return projectUid;
	}

	public void setProjectUid(Long projectUid) {
		this.projectUid = projectUid;
	}

	public Map<String, Object> getContents() {
		return contents;
	}

	public void setContents(Map<String, Object> contents) {
		this.contents = contents;
	}
	
	
}
