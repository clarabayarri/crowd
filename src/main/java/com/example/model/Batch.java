package com.example.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Batch {

	@Id
    @GeneratedValue
    private Integer id;
	
	private String name;
	
	private Integer executionsPerTask;
	
	private double percentageComplete;
	
	@OneToMany
	private Set<Task> tasks;

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
		updatePercentageComplete();
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
		updatePercentageComplete();
	}

	public double getPercentageComplete() {
		return percentageComplete;
	}
	
	public void updatePercentageComplete() {
		if (tasks != null) {
			int total = 0;
			for(Task task : tasks) {
				total += Math.min(executionsPerTask, task.getNumExecutions());
			}
			
			if (total > 0) {
				this.percentageComplete = ((double) total * 100) / (executionsPerTask * tasks.size());
			}
		}
	}
}
