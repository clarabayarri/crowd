package com.crowdplatform.service;

import java.util.List;
import java.util.Map;

import com.crowdplatform.model.Batch;
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
	
	public Map<Object, Object> getAggregatedDataByDate(Project project);
	
	public Map<Object, Object> getAggregatedDataByField(Project project, String field);
	
	public Map<Object, Object> getAggregatedDataByMultivaluateField(Project project, String field);
	
	public  Map<Object, Object> getAggregatedDataByFieldWithSteps(Project project, String field);
	
	public Map<Object, Object> getAggregatedDataByDate(Project project, Batch batch);
	
	public Map<Object, Object> getAggregatedDataByField(Project project, Batch batch, String field);
	
	public Map<Object, Object> getAggregatedDataByMultivaluateField(Project project, Batch batch, String field);
	
	public  Map<Object, Object> getAggregatedDataByFieldWithSteps(Project project, Batch batch, String field);
}
