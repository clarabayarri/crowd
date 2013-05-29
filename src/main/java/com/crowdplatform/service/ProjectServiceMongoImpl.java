package com.crowdplatform.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DefaultIndexOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.MapReduceResult;
import com.crowdplatform.model.Project;
import com.crowdplatform.util.DataMiner;
import com.google.common.collect.Maps;

@Service
public class ProjectServiceMongoImpl implements ProjectService {

	@Autowired
	MongoOperations mongoOperation;
	
	@Autowired
	DataMiner dataMiner;
	
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
	
	public List<Project> getProjectsForUser(String userId) {
		checkForOwnerIndex();
		Query query = new Query(Criteria.where("ownerId").is(userId));
		List<Project> projects = mongoOperation.find(query, Project.class);
		return projects;
	}
	
	private void checkForOwnerIndex() {
		DefaultIndexOperations indexOperations = new DefaultIndexOperations(mongoOperation, "project");
		indexOperations.ensureIndex(new Index().on("ownerId", Order.ASCENDING));
	}
	
	public Map<Object, Object> getAggregatedDataByDate(Project project) {
		return transform(dataMiner.aggregateByDate(project));
	}
	
	public Map<Object, Object> getAggregatedDataByField(Project project, String field) {
		return transform(dataMiner.aggregateByField(project, field));
	}
	
	public Map<Object, Object> getAggregatedDataByMultivaluateField(Project project, String field) {
		return transform(dataMiner.aggregateByMultivaluateField(project, field));
	}
	
	public Map<Object, Object> getAggregatedDataByFieldWithSteps(Project project, String field) {
		return dataMiner.aggregateByFieldWithIntegerSteps(project, field);
	}
	
	public Map<Object, Object> getAggregatedDataByDate(Project project, Batch batch) {
		return transform(dataMiner.aggregateByDate(project, batch));
	}
	
	public Map<Object, Object> getAggregatedDataByField(Project project, Batch batch, String field) {
		return transform(dataMiner.aggregateByField(project, batch, field));
	}
	
	public Map<Object, Object> getAggregatedDataByMultivaluateField(Project project, Batch batch, String field) {
		return transform(dataMiner.aggregateByMultivaluateField(project, batch, field));
	}
	
	public Map<Object, Object> getAggregatedDataByFieldWithSteps(Project project, Batch batch, String field) {
		return dataMiner.aggregateByFieldWithIntegerSteps(project, batch, field);
	}
	
	private Map<Object, Object> transform(MapReduceResults<MapReduceResult> results) {
		Map<Object, Object> map = Maps.newLinkedHashMap();
		for (MapReduceResult result : results) {
			map.put(result.getId(), result.getValue());
		}
		return map;
	}
}
