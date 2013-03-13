package com.crowdplatform.model;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Field {

	@Id
    @GeneratedValue
    private Integer id;
	
	@NotNull
	private String name;
	
	@ElementCollection
	private Set<String> columnNames;
	
	public enum Type {
		INTEGER, DOUBLE, STRING, MULTIVALUATE_STRING
	}
	
	@NotNull
	private Type type;

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

	public Set<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(Set<String> columnNames) {
		this.columnNames = columnNames;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
