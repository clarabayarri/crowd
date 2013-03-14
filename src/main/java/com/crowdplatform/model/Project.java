package com.crowdplatform.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Entity
public class Project {

	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	@Size(min=4)
	private String name;
	
	@OneToMany
	private Set<Batch> batches;
	
	@OneToMany
	private Set<Field> inputFields;
	
	@OneToMany
	private Set<Field> outputFields;
	
	public Project() {
		batches = Sets.newHashSet();
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

	public Set<Batch> getBatches() {
		return batches;
	}

	public void setBatches(Set<Batch> batches) {
		this.batches = batches;
	}
	
	public void addBatch(Batch batch) {
		this.batches.add(batch);
	}

	public Set<Field> getInputFields() {
		return inputFields;
	}

	public void setInputFields(Set<Field> inputFields) {
		this.inputFields = inputFields;
	}
	
	public Set<Field> getOutputFields() {
		return outputFields;
	}

	public void setOutputFields(Set<Field> outputFields) {
		this.outputFields = outputFields;
	}

	public List<Field> getOrderedInputFields() {
		List<Field> list = Lists.newArrayList();
		list.addAll(this.inputFields);
		Collections.sort(list, new Comparator<Field>() {

			@Override
			public int compare(Field arg0, Field arg1) {
				return arg0.getId().compareTo(arg1.getId());
			}});
		return list;
	}
	
	public List<Field> getOrderedOutputFields() {
		List<Field> list = Lists.newArrayList();
		list.addAll(this.outputFields);
		Collections.sort(list, new Comparator<Field>() {

			@Override
			public int compare(Field arg0, Field arg1) {
				return arg0.getId().compareTo(arg1.getId());
			}});
		return list;
	}
	
	public List<Batch> getOrderedBatches() {
		List<Batch> list = Lists.newArrayList();
		list.addAll(this.batches);
		Collections.sort(list, new Comparator<Batch>() {

			@Override
			public int compare(Batch b1, Batch b2) {
				return b1.getCreationDate().compareTo(b2.getCreationDate());
			}});
		return list;
	}
}
