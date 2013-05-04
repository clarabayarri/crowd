package com.crowdplatform.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Lists;

@Document
public class Project {

	private String id;
	
	private Long uid;
	
	@NotNull
	@Size(min=4)
	private String name;
	
	private Date creationDate;
	
	private List<Batch> batches;
	
	private List<Field> inputFields;
	
	private List<Field> outputFields;
	
	private List<Field> userFields;
	
	private String ownerId;
	
	private List<ProjectUser> users;
	
	public Project() {
		batches = Lists.newArrayList();
		inputFields = Lists.newArrayList();
		outputFields = Lists.newArrayList();
		userFields = Lists.newArrayList();
		users = Lists.newArrayList();
		creationDate = new Date();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Batch> getBatches() {
		return batches;
	}

	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}
	
	public void addBatch(Batch batch) {
		this.batches.add(batch);
	}
	
	public void removeBatch(Integer batchId) {
		for (Batch batch : this.batches) {
			if (batchId.equals(batch.getId())) {
				this.batches.remove(batch);
				return;
			}
		}
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public List<ProjectUser> getUsers() {
		return users;
	}

	public void setUsers(List<ProjectUser> users) {
		this.users = users;
	}
	
	public void addUser(ProjectUser user) {
		this.users.add(user);
	}

	public List<Field> getInputFields() {
		return inputFields;
	}

	public void setInputFields(List<Field> inputFields) {
		this.inputFields = inputFields;
	}
	
	public void addInputField(Field field) {
		this.inputFields.add(field);
	}

	public List<Field> getOutputFields() {
		return outputFields;
	}

	public void setOutputFields(List<Field> outputFields) {
		this.outputFields = outputFields;
	}
	
	public void addOutputField(Field field) {
		this.outputFields.add(field);
	}

	public List<Field> getUserFields() {
		return userFields;
	}

	public void setUserFields(List<Field> userFields) {
		this.userFields = userFields;
	}
	
	public void addUserField(Field field) {
		this.userFields.add(field);
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getNumUsers() {
		return users.size();
	}
}
