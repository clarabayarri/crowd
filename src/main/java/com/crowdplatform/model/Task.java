package com.crowdplatform.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Formula;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Entity
public class Task {

	@Id
    @GeneratedValue
    private Integer id;
	
	private String contents;
	
	@OneToMany
	@Cascade({CascadeType.ALL})
	Set<Execution> executions;
	
	@Formula("(select count(*) from task_execution te where te.task_id=id)")
	private Integer numExecutions;

	public Task() {
		this.numExecutions = 0;
		this.executions = Sets.newHashSet();
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
	
	public Map<String, String> getContentsMap() {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> result = Maps.newHashMap();
		JsonNode node;
		try {
			node = mapper.readTree(this.contents);
			Iterator<String> it = node.getFieldNames();
			while (it.hasNext()) {
				String fieldName = it.next();
				JsonNode child = node.get(fieldName);
				result.put(fieldName, child.toString());
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getNumExecutions() {
		return numExecutions;
	}

	public Set<Execution> getExecutions() {
		return executions;
	}

	public void setExecutions(Set<Execution> executions) {
		this.executions = executions;
	}
	
	public void addExecution(Execution execution) {
		this.executions.add(execution);
	}

	public void setNumExecutions(Integer numExecutions) {
		this.numExecutions = numExecutions;
	}
	
	
}
