package com.crowdplatform.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Entity
public class PlatformUser {

	@Id
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="email", unique=true)
	private String email;
	
	@OneToMany(fetch=FetchType.EAGER)
	@Cascade({CascadeType.ALL})
	Set<Project> projects;

	public PlatformUser() {
		this.projects = Sets.newHashSet();
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

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
	}
	
	public void removeProject(Long projectId) {
		for (Project project : this.projects) {
			if (project.getId().equals(projectId)) {
				this.projects.remove(project);
				break;
			}
		}
	}
	
	public List<Project> getOrderedProjects() {
		List<Project> list = Lists.newArrayList();
		list.addAll(this.projects);
		Collections.sort(list, new Comparator<Project>() {

			@Override
			public int compare(Project p1, Project p2) {
				return p1.getCreationDate().compareTo(p2.getCreationDate());
			}});
		return list;
	}
	
	public boolean isOwnerOfProject(Long projectId) {
		for (Project project : this.projects) {
			if (projectId.equals(project.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOwnerOfBatch(Long projectId, Integer batchId) {
		for (Project project : this.projects) {
			if (projectId.equals(project.getId())) {
				for (Batch batch : project.getBatches()) {
					if (batchId.equals(batch.getId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
