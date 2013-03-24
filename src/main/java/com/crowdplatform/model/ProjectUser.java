package com.crowdplatform.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProjectUser {

	@Id
	private String username;
	
	private String contents;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	

}
