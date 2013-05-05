package com.crowdplatform.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Maps;

public class Task {

    private Integer id;
	
	private String contents;
	
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

	public void setNumExecutions(Integer numExecutions) {
		this.numExecutions = numExecutions;
	}
}
