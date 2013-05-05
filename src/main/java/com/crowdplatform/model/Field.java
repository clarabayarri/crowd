package com.crowdplatform.model;

import java.util.Set;

import javax.validation.constraints.NotNull;

public class Field {

    private Integer id;
	
	@NotNull
	private String name;
	
	private Set<String> columnNames;
	
	public enum Type {
		INTEGER, DOUBLE, STRING, MULTIVALUATE_STRING, BOOL
	}
	
	@NotNull
	private Type type;
	
	public enum FieldType {
		INPUT, OUTPUT, USER
	}
	
	@NotNull
	private FieldType fieldType;

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

	public FieldType getFieldType() {
		return fieldType;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
}
