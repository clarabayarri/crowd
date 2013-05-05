package com.crowdplatform.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;

@Document
public class BatchExecutionCollection {

	private String id;
	
	private String projectId;
	
	private Long batchId;
	
	private List<Execution> executions;
	
	public BatchExecutionCollection() {
		this.executions = Lists.newArrayList();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public List<Execution> getExecutions() {
		return executions;
	}

	public void setExecutions(List<Execution> executions) {
		this.executions = executions;
	}
	
	public void addExecution(Execution execution) {
		execution.setId(this.executions.size() + 1);
		this.executions.add(execution);
	}
}
