package com.crowdplatform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(MockitoJUnitRunner.class)
public class FileWriterTest {

	@InjectMocks
	private FileWriter writer = new FileWriter();
	
	@Test
	public void testWriteExecutionsReturnsString() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		Project project = new Project();
		project.setInputFields(fields);
		project.setOutputFields(fields);
		project.setUserFields(fields);
		Batch batch = new Batch();
		batch.setTasks(tasks);
		BatchExecutionCollection collection = new BatchExecutionCollection();
		
		String result = writer.writeTasksExecutions(project, batch, collection, true);
		
		assertNotNull(result);
	}
	
	@Test
	public void testWriteExecutionsReturnsHeaderRow() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		Project project = new Project();
		project.setInputFields(fields);
		project.setOutputFields(fields);
		project.setUserFields(fields);
		Batch batch = new Batch();
		batch.setTasks(tasks);
		BatchExecutionCollection collection = new BatchExecutionCollection();
		
		String result = writer.writeTasksExecutions(project, batch, collection, true);
		
		String expected = "\"task_id\",\"execution_id\",\"date\",\"userId\"\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void testWriteExecutionsReturnsHeaderRowWithFields() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		Field field = new Field();
		field.setName("field");
		List<Field> fields = Lists.newArrayList(field);
		Field field2 = new Field();
		field2.setName("field2");
		List<Field> fields2 = Lists.newArrayList(field2);
		List<Field> fields3 = Lists.newArrayList();
		Project project = new Project();
		project.setInputFields(fields);
		project.setOutputFields(fields2);
		project.setUserFields(fields3);
		Batch batch = new Batch();
		batch.setTasks(tasks);
		BatchExecutionCollection collection = new BatchExecutionCollection();
		
		String result = writer.writeTasksExecutions(project, batch, collection, true);
		
		String expected = "\"task_id\",\"field\",\"execution_id\",\"date\",\"userId\",\"field2\"\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void testWriteHeaders() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		Field field2 = new Field();
		field2.setName("field2");
		field2.setType(Field.Type.STRING);
		List<Field> fields2 = Lists.newArrayList(field2);
		Field field3 = new Field();
		field3.setName("field3");
		field3.setType(Field.Type.STRING);
		List<Field> fields3 = Lists.newArrayList(field3);
		
		String[] result = writer.writeHeaders(fields, fields2, fields3);
		
		assertEquals(FileWriter.NUM_STATIC_TASK_FIELDS + FileWriter.NUM_STATIC_EXECUTION_FIELDS + 
				fields.size() + fields2.size() + fields3.size(), result.length);
		assertEquals("task_id", result[0]);
		assertEquals("field", result[FileWriter.NUM_STATIC_TASK_FIELDS]);
		assertEquals("execution_id", result[FileWriter.NUM_STATIC_TASK_FIELDS + fields.size()]);
		assertEquals("date", result[FileWriter.NUM_STATIC_TASK_FIELDS + fields.size() + 1]);
		assertEquals("userId", result[FileWriter.NUM_STATIC_TASK_FIELDS + fields.size() + 2]);
		assertEquals("field2", result[FileWriter.NUM_STATIC_TASK_FIELDS + fields.size() + FileWriter.NUM_STATIC_EXECUTION_FIELDS]);
		assertEquals("field3", result[FileWriter.NUM_STATIC_TASK_FIELDS + fields.size() + FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields2.size()]);
	}
	
	@Test
	public void testDecodeTask() {
		Task task = new Task();
		task.setId(2);
		task.setContents(getStringContents());
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decodeTask(task, fields);
		
		assertEquals(FileWriter.NUM_STATIC_TASK_FIELDS + fields.size(), result.length);
		assertEquals("2", result[0]);
		assertEquals("string", result[1]);
	}
	
	@Test
	public void testDecodeExecutionWithoutUser() {
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents(getEmptyContents());
		List<Field> fields = Lists.newArrayList();
		
		String[] result = writer.decodeExecution(execution, null, fields, fields);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS, result.length);
		assertEquals("2", result[0]);
		assertTrue(!result[1].isEmpty());
		assertNull(result[2]);
	}
	
	@Test
	public void testDecodeExecutionWithoutUserWithUserFields() {
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents(getEmptyContents());
		List<Field> fields = Lists.newArrayList();
		Field field = new Field();
		List<Field> fields2 = Lists.newArrayList(field);
		
		String[] result = writer.decodeExecution(execution, null, fields, fields2);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields2.size(), result.length);
		assertNull(result[2]);
		assertNull(result[FileWriter.NUM_STATIC_EXECUTION_FIELDS]);
	}
	
	@Test
	public void testDecodeExecutionWithEmptyUser() {
		ProjectUser user = new ProjectUser();
		user.setId(3);
		user.setContents(getEmptyContents());
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents(getEmptyContents());
		execution.setProjectUserId(user.getId());
		List<Field> fields = Lists.newArrayList();
		
		String[] result = writer.decodeExecution(execution, user, fields, fields);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS, result.length);
		assertEquals("3", result[2]);
	}
	
	@Test
	public void testDecodeExecutionWithFields() {
		ProjectUser user = new ProjectUser();
		user.setId(3);
		user.setContents(getStringContents());
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents(getStringContents());
		execution.setProjectUserId(user.getId());
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decodeExecution(execution, user, fields, fields);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields.size() + fields.size(), 
				result.length);
		assertEquals("string", result[FileWriter.NUM_STATIC_EXECUTION_FIELDS]);
		assertEquals("string", result[FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields.size()]);
	}
	
	@Test
	public void testDecodeAddsStringFields() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(getStringContents(), fields);
		
		assertEquals(1, result.length);
		assertEquals("string", result[0]);
	}
	
	@Test
	public void testDecodeAddsIntegerFields() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.INTEGER);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(getIntegerContents(), fields);
		
		assertEquals(1, result.length);
		assertEquals("1", result[0]);
	}
	
	@Test
	public void testDecodeAddsDoubleFields() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.DOUBLE);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(getDoubleContents(), fields);
		
		assertEquals(1, result.length);
		assertEquals("1.3", result[0]);
	}
	
	@Test
	public void testDecodeAddsMultivaluateStringFields() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.MULTIVALUATE_STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(getMultivaluateStringContents(), fields);
		
		assertEquals(1, result.length);
		assertEquals("a,b,", result[0]);
	}
	
	@Test
	public void testDecodeAddsBooleanFields() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.BOOL);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(getBooleanContents(), fields);
		
		assertEquals(1, result.length);
		assertEquals("true", result[0]);
	}
	
	@Test
	public void testDecodeAddsSeveralFields() {
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.MULTIVALUATE_STRING);
		Field field2 = new Field();
		field2.setName("field2");
		field2.setType(Field.Type.BOOL);
		Field field3 = new Field();
		field3.setName("field3");
		field3.setType(Field.Type.INTEGER);
		List<Field> fields = Lists.newArrayList(field, field2, field3);
		
		String[] result = writer.decode(getMixedContents(), fields);
		
		assertEquals(3, result.length);
		assertEquals("a,b,", result[0]);
		assertEquals("true", result[1]);
		assertEquals("1", result[2]);
	}
	
	private Map<String, Object> getEmptyContents() {
		Map<String, Object> result = Maps.newHashMap();
		return result;
	}
	
	private Map<String, Object> getStringContents() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("field", "string");
		return result;
	}
	
	private Map<String, Object> getIntegerContents() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("field", 1);
		return result;
	}
	
	private Map<String, Object> getDoubleContents() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("field", 1.3);
		return result;
	}
	
	private Map<String, Object> getBooleanContents() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("field", true);
		return result;
	}
	
	private Map<String, Object> getMultivaluateStringContents() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("field", Lists.newArrayList("a", "b"));
		return result;
	}
	
	private Map<String, Object> getMixedContents() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("field", Lists.newArrayList("a", "b"));
		result.put("field2", true);
		result.put("field3", 1);
		return result;
	}
}
