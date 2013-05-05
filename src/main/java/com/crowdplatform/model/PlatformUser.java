package com.crowdplatform.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import com.google.common.collect.Lists;

@Entity
public class PlatformUser {

	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email", unique=true)
	private String email;
	
	@ElementCollection(fetch=FetchType.EAGER)
	List<String> projects;

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

}
