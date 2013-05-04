package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Project;

public interface ProjectService {
	
	/**
	 * Persist a new project.
	 * @param project
	 */
	public void addProject(Project project);
	
	/**
	 * Save an existing project.
	 * @param project
	 */
	public void saveProject(Project project);
	
	/**
	 * Remove an existing project.
	 * @param id The project id
	 */
	public void removeProject(String id);

	/**
	 * Retrieve a persisted project.
	 * @param id The project id
	 * @return Project corresponding to id
	 */
	public Project getProject(String id);
	
	public List<Project> getProjectsForUser(String userId);
	
}
