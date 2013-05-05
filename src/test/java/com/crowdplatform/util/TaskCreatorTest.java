package com.crowdplatform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Task;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class TaskCreatorTest {

	private TaskCreator service = new TaskCreator();
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTasksReadsStringFields() throws JsonProcessingException, IOException {
		Batch batch = new Batch();
		List<Field> fields = createTestFields();
		Map<String, String> data = createTestDataMap();
		
		service.createTasks(batch, fields, Lists.newArrayList(data));
	
		List<Task> tasks = batch.getTasks();
		assertEquals(1, tasks.size());
		for (Task task : tasks) {
			JsonNode node = (new ObjectMapper()).readTree(task.getContents());
			assertTrue(node.get("text").isTextual());
			assertEquals("bla", node.get("text").getTextValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTasksReadsIntegerFields() throws JsonProcessingException, IOException {
		Batch batch = new Batch();
		List<Field> fields = createTestFields();
		Map<String, String> data = createTestDataMap();
		
		service.createTasks(batch, fields, Lists.newArrayList(data));
	
		List<Task> tasks = batch.getTasks();
		assertEquals(1, tasks.size());
		for (Task task : tasks) {
			JsonNode node = (new ObjectMapper()).readTree(task.getContents());
			assertTrue(node.get("integer").isInt());
			assertEquals(33, node.get("integer").getIntValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTasksReadsFloatFields() throws JsonProcessingException, IOException {
		Batch batch = new Batch();
		List<Field> fields = createTestFields();
		Map<String, String> data = createTestDataMap();
		
		service.createTasks(batch, fields, Lists.newArrayList(data));
	
		List<Task> tasks = batch.getTasks();
		assertEquals(1, tasks.size());
		for (Task task : tasks) {
			JsonNode node = (new ObjectMapper()).readTree(task.getContents());
			assertTrue(node.get("float").isFloatingPointNumber());
			assertEquals(33.3, node.get("float").getDoubleValue(), 0.0001);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTasksReadsNullValues() throws JsonProcessingException, IOException {
		Batch batch = new Batch();
		List<Field> fields = createTestFields();
		Map<String, String> data = createTestDataMap();
		
		service.createTasks(batch, fields, Lists.newArrayList(data));
	
		List<Task> tasks = batch.getTasks();
		assertEquals(1, tasks.size());
		for (Task task : tasks) {
			JsonNode node = (new ObjectMapper()).readTree(task.getContents());
			assertTrue(node.get("null").isNull());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateTasksReadsMultivaluateStringFields() throws JsonProcessingException, IOException {
		Batch batch = new Batch();
		List<Field> fields = createTestFields();
		Map<String, String> data = createTestDataMap();
		
		service.createTasks(batch, fields, Lists.newArrayList(data));
	
		List<Task> tasks = batch.getTasks();
		assertEquals(1, tasks.size());
		for (Task task : tasks) {
			JsonNode node = (new ObjectMapper()).readTree(task.getContents());
			assertTrue(node.get("multi").isArray());
			assertEquals(2, node.get("multi").size());
		}
	}
	
	private List<Field> createTestFields() {
		Field textField = new Field();
		textField.setName("text");
		textField.setType(Field.Type.STRING);
		Field integerField = new Field();
		integerField.setName("integer");
		integerField.setType(Field.Type.INTEGER);
		Field floatField = new Field();
		floatField.setName("float");
		floatField.setType(Field.Type.DOUBLE);
		Field nullField = new Field();
		nullField.setName("null");
		nullField.setType(Field.Type.STRING);
		Field multiField = new Field();
		multiField.setName("multi");
		multiField.setType(Field.Type.MULTIVALUATE_STRING);
		multiField.setColumnNames(Sets.newHashSet("field_1", "field_2"));
		return Lists.newArrayList(textField, integerField, floatField, nullField, multiField);
	}
	
	private Map<String, String> createTestDataMap() {
		Map<String, String> data = Maps.newHashMap();
		data.put("text", "bla");
		data.put("integer", "33");
		data.put("float", "33.3");
		data.put("null", null);
		data.put("field_1", "a");
		data.put("field_2", "b");
		return data;
	}
}
