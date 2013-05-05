package com.crowdplatform.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.collect.Lists;

public class Batch {

	private Integer id;
	
	@NotNull
	@Size(min=3)
	private String name;
	
	@NotNull
	@Min(1)
	private Integer executionsPerTask;
	
	private Date creationDate;
	
	private double percentageComplete;
	
	private String fusiontableId;
	
	private String executionCollectionId;
	
	public enum State {
		RUNNING, PAUSED, COMPLETE
	}
	
	private State state;
	
	private List<Task> tasks;

	public Batch() {
		creationDate = new Date();
		tasks = Lists.newArrayList();
		executionsPerTask = 1;
		state = State.PAUSED;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getExecutionsPerTask() {
		return executionsPerTask;
	}

	public void setExecutionsPerTask(Integer executionsPerTask) {
		this.executionsPerTask = executionsPerTask;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addTask(Task task) {
		task.setId(this.tasks.size() + 1);
		this.tasks.add(task);
	}
	
	public Task getTask(Integer taskId) {
		for (Task task : this.tasks) {
			if (taskId.equals(task.getId()))
				return task;
		}
		return null;
	}

	public double getPercentageComplete() {
		if (tasks != null) {
			int total = 0;
			for(Task task : tasks) {
				total += Math.min(executionsPerTask, task.getNumExecutions());
			}
			if (total > 0) {
				this.percentageComplete = ((double) total * 100) / (executionsPerTask * tasks.size());
			}
		}
		return percentageComplete;
	}

	public Integer getNumTasks() {
		return tasks.size();
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getFusiontableId() {
		return fusiontableId;
	}

	public void setFusiontableId(String fusiontableId) {
		this.fusiontableId = fusiontableId;
	}

	public String getExecutionCollectionId() {
		return executionCollectionId;
	}

	public void setExecutionCollectionId(String executionCollectionId) {
		this.executionCollectionId = executionCollectionId;
	}
}
