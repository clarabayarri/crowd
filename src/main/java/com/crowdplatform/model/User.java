package com.crowdplatform.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Entity
public class User {

	@Id
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	private String email;
	
	@OneToMany
	Set<Project> projects;

	public User() {
		this.projects = Sets.newHashSet();
	}
	
	public User(Registration registration) {
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

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
	}
	
	public List<Project> getOrderedProjects() {
		List<Project> list = Lists.newArrayList();
		list.addAll(this.projects);
		Collections.sort(list, new Comparator<Project>() {

			@Override
			public int compare(Project p1, Project p2) {
				// TODO Auto-generated method stub
				return p1.getCreationDate().compareTo(p2.getCreationDate());
			}});
		return list;
	}

}
