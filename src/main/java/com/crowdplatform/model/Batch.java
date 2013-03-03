package com.crowdplatform.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;


@Entity
public class Batch {

	@Id
    @GeneratedValue
    private Integer id;
	
	@NotNull
	@Size(min=3)
	private String name;
	
	@NotNull
	@Min(1)
	private Integer executionsPerTask;
	
	private double percentageComplete;
	
	@ManyToOne
	@JoinColumn(name="Project_id")
	private Project project;
	
	public enum State {
		RUNNING, PAUSED, COMPLETE
	}
	
	@Enumerated(EnumType.STRING)
	private State state;
	
	@OneToMany
	private Set<Task> tasks;
	
	@Formula("(select count(*) from Task t where t.Batch_id=id)")
	private Integer numTasks;

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

	public Integer getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(Integer numTasks) {
		this.numTasks = numTasks;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
