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
		if (this.batches.isEmpty()) {
			batch.setId(1);
		} else {
			int lastId = this.batches.get(this.batches.size()-1).getId();
			batch.setId(lastId + 1);
		}
		this.batches.add(batch);
	}
	
	public Batch getBatch(Integer batchId) {
		for (Batch batch : this.batches) {
			if (batchId.equals(batch.getId())) {
				return batch;
			}
		}
		return null;
	}
	
	public void removeBatch(Integer batchId) {
		for (Batch batch : this.batches) {
			if (batchId.equals(batch.getId())) {
				this.batches.remove(batch);
				return;
			}
		}
	}
	
	public List<Batch> getRunningBatches() {
		List<Batch> batches = Lists.newArrayList();
		for (Batch batch : this.batches) {
			if (batch.getState() == Batch.State.RUNNING)
				batches.add(batch);
		}
		return batches;
	}
	
	public List<Batch> getCompletedBatches() {
		List<Batch> batches = Lists.newArrayList();
		for (Batch batch : this.batches) {
			if (batch.getState() == Batch.State.COMPLETE)
				batches.add(batch);
		}
		return batches;
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
		user.setId(this.users.size() + 1);
		this.users.add(user);
	}

	public ProjectUser getUser(Integer id) {
		for (ProjectUser user : this.users) {
			if (id.equals(user.getId())) {
				return user;
			}
		}
		return null;
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

	public Field getField(String fieldName) {
		for (Field field : this.inputFields) {
			if (field.getName().equals(fieldName)) return field;
		}
		for (Field field : this.outputFields) {
			if (field.getName().equals(fieldName)) return field;
		}
		for (Field field : this.userFields) {
			if (field.getName().equals(fieldName)) return field;
		}
		return null;
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
