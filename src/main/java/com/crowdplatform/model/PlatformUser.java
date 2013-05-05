package com.crowdplatform.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;

@Document
public class PlatformUser {

	@Id
	private String username;
	
	private String password;
	
	private String email;
	
	private List<String> projects;
	
	private PasswordResetRequest passwordResetRequest;

	public PlatformUser() {
		this.projects = Lists.newArrayList();
	}
	
	public PlatformUser(Registration registration) {
		this.username = registration.getUsername();
		this.password = registration.getPassword();
		this.email = registration.getEmail();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}
	
	public void addProject(String project) {
		this.projects.add(project);
	}
	
	public void removeProject(String projectId) {
		for (String project : this.projects) {
			if (project.equals(projectId)) {
				this.projects.remove(project);
				break;
			}
		}
	}
	
	public boolean isOwnerOfProject(String projectId) {
		for (String project : this.projects) {
			if (projectId.equals(project)) {
				return true;
			}
		}
		return false;
	}

	public PasswordResetRequest getPasswordResetRequest() {
		return passwordResetRequest;
	}

	public void setPasswordResetRequest(PasswordResetRequest passwordResetRequest) {
		this.passwordResetRequest = passwordResetRequest;
	}

}
