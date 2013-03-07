package com.crowdplatform.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Formula;

@Entity
public class Task {

	@Id
    @GeneratedValue
    private Integer id;
	
	private String contents;
	
	@ManyToOne
	@JoinColumn(name="Batch_id")
	private Batch batch;
	
	@OneToMany
	Set<Execution> executions;
	
	@Formula("(select count(*) from Execution e where e.Task_id=id)")
	private Integer numExecutions;

	public Task() {
		this.numExecutions = 0;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public int getNumExecutions() {
		return numExecutions;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Set<Execution> getExecutions() {
		return executions;
	}

	public void setExecutions(Set<Execution> executions) {
		this.executions = executions;
	}

	public void setNumExecutions(Integer numExecutions) {
		this.numExecutions = numExecutions;
	}
	
	
}
