package com.crowdplatform.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Execution {

	@Id
    @GeneratedValue
    private Integer id;
	
	private Date date;
	
	private String contents;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private ProjectUser projectUser;
	
	public Execution() {
		this.date = new Date();
	}
	
	public Execution(String contents) {
		this.contents = contents;
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

	public ProjectUser getProjectUser() {
		return projectUser;
	}

	public void setProjectUser(ProjectUser projectUser) {
		this.projectUser = projectUser;
	}
}
