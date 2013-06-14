package com.crowdplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DefaultIndexOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;

@Service
public class ProjectServiceMongoImpl implements ProjectService {

	@Autowired
	MongoOperations mongoOperation;
	
	public void addProject(Project project) {
		mongoOperation.save(project);
	}
	
	public void saveProject(Project project) {
		mongoOperation.save(project);
	}
	
	public void removeProject(String id) {
		Project project = getProject(id);
		if (project != null) {
			mongoOperation.remove(project);
		} else {
			System.out.println("ProjectServiceMongoImpl: Could not remove project, project not found.");
		}
	}
	
	public Project getProject(String id) {
		return mongoOperation.findById(id, Project.class);
	}
	
	public List<Project> getProjectsForUser(PlatformUser user) {
		checkForOwnerIndex();
		Query query = new Query(Criteria.where("ownerId").is(user.getUsername()));
		List<Project> projects = mongoOperation.find(query, Project.class);
		return projects;
	}
	
	private void checkForOwnerIndex() {
		DefaultIndexOperations indexOperations = new DefaultIndexOperations(mongoOperation, "project");
		indexOperations.ensureIndex(new Index().on("ownerId", Order.ASCENDING));
	}
}
