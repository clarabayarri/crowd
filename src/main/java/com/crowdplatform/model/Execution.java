package com.crowdplatform.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Execution {

	@Id
    @GeneratedValue
    private Integer id;
	
	private Date date;
	
	private String contents;
	
	@ManyToOne
	@JoinColumn(name="Task_id")
	private Task task;
	
	public Execution() {
		this.date = new Date();
	}
	
	public Execution(String contents, Task task) {
		this.contents = contents;
		this.task = task;
		this.date = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	
	
}
