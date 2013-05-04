package com.crowdplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.crowdplatform.model.Project;

public class ProjectServiceMongoImpl implements ProjectService {

	@Autowired
	MongoOperations mongoOperation;
	
	public void addProject(Project project) {
		mongoOperation.save(project);
	}
	
	public void saveProject(Project project) {
		mongoOperation.save(project);
	}
	
	public void removeProject(Long id) {
		Project project = getProject(id);
		if (project != null) {
			mongoOperation.remove(project);
		}
	}
	
	public Project getProject(Long id) {
		return mongoOperation.findById(id, Project.class);
	}
}
