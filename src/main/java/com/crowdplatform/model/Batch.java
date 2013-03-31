package com.crowdplatform.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


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
	
	private Date creationDate;
	
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
	@Cascade({CascadeType.ALL})
	private Set<Task> tasks;
	
	@Formula("(select count(*) from Task t where t.Batch_id=id)")
	private Integer numTasks;

	public Batch() {
		creationDate = new Date();
		tasks = Sets.newHashSet();
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

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
		updatePercentageComplete();
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public List<Task> getOrderedTasks() {
		List<Task> list = Lists.newArrayList();
		list.addAll(this.tasks);
		Collections.sort(list, new Comparator<Task>() {

			@Override
			public int compare(Task o1, Task o2) {
				return o1.getId().compareTo(o2.getId());
			}});
		return list;
	}
}
