package com.crowdplatform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Task;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class FileWriterTest {

	private FileWriter writer = new FileWriter();

	@Test
	public void testWriteExecutionsReturnsString() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(tasks, fields, fields);
		
		assertNotNull(result);
	}
	
	@Test
	public void testWriteExecutionsReturnsHeaderRow() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(tasks, fields, fields);
		
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
		
		String result = writer.writeTasksExecutions(tasks, fields, fields2);
		
		String expected = "\"task_id\",\"field\",\"execution_id\",\"date\",\"userId\",\"field2\"\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionBasicInformation() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		task.setExecutions(Sets.newHashSet(execution));
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields);
		
		String expected = "\"task_id\",\"execution_id\",\"date\",\"userId\"\n\"1\",\"2\",";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsTaskFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{\"field\":\"contents\"}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		List<Field> fields2 = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = "\n\"1\",\"contents\"";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{\"field\":\"contents\"}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList();
		List<Field> fields2 = Lists.newArrayList(field);
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = ",\"contents\"\n";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsTaskIntegerFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{\"field\":1}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.INTEGER);
		List<Field> fields = Lists.newArrayList(field);
		List<Field> fields2 = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = "\n\"1\",\"1\"";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionIntegerFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{\"field\":1}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.INTEGER);
		List<Field> fields = Lists.newArrayList();
		List<Field> fields2 = Lists.newArrayList(field);
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = ",\"1\"\n";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteTasksAddsExecutionDoubleFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{\"field\":1.07}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.DOUBLE);
		List<Field> fields = Lists.newArrayList(field);
		List<Field> fields2 = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = "\n\"1\",\"1.07\"";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionDoubleFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{\"field\":1.07}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.DOUBLE);
		List<Field> fields = Lists.newArrayList();
		List<Field> fields2 = Lists.newArrayList(field);
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = ",\"1.07\"\n";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteTasksAddsExecutionMultivaluateFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{\"field\":[\"a\",\"b\"]}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.MULTIVALUATE_STRING);
		List<Field> fields = Lists.newArrayList(field);
		List<Field> fields2 = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = "\n\"1\",\"a,b,\"";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionMultivaluateFields() throws IOException {
		Task task = new Task();
		task.setId(1);
		task.setContents("{}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{\"field\":[\"a\",\"b\"]}");
		task.setExecutions(Sets.newHashSet(execution));
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.MULTIVALUATE_STRING);
		List<Field> fields = Lists.newArrayList();
		List<Field> fields2 = Lists.newArrayList(field);
		
		String result = writer.writeTasksExecutions(Lists.newArrayList(task), fields, fields2);
		
		String expected = ",\"a,b,\"\n";
		assertTrue(result.contains(expected));
	}
}
