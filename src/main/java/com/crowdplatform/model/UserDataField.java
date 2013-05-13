package com.crowdplatform.model;

import org.springframework.data.annotation.Id;

public class UserDataField {
	
	@Id
	private String name;

	private String projectFieldName;
	
	public enum UserDataFieldAggregationType {
		SUM, AVERAGE
	}
	
	private UserDataFieldAggregationType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectFieldName() {
		return projectFieldName;
	}

	public void setProjectFieldName(String projectFieldName) {
		this.projectFieldName = projectFieldName;
	}

	public UserDataFieldAggregationType getType() {
		return type;
	}

	public void setType(UserDataFieldAggregationType type) {
		this.type = type;
	}
	
	
}
