package com.example.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
		return executions.size();
	}
	
}
