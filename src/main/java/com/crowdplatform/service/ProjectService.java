package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Project;

public interface ProjectService {

	public Project getProject(Integer id);
	
	public List<Project> listProjects();
	
	public void addProject(Project project);
	
	public void removeProject(Integer id);
	
	public void saveProject(Project project);
}
