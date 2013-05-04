package com.crowdplatform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.crowdplatform.service.ProjectUserService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class FileWriterTest {

	@InjectMocks
	private FileWriter writer = new FileWriter();

	@Mock
	private ProjectUserService userService;
	
	@Test
	public void testWriteExecutionsReturnsString() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(tasks, fields, fields, fields, true);
		
		assertNotNull(result);
	}
	
	@Test
	public void testWriteExecutionsReturnsHeaderRow() throws IOException {
		List<Task> tasks = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeTasksExecutions(tasks, fields, fields, fields, true);
		
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
		
		String result = writer.writeTasksExecutions(tasks, fields, fields2, fields3, true);
		
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
		task.setContents("{\"field\":\"string\"}");
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
		execution.setContents("{}");
		List<Field> fields = Lists.newArrayList();
		
		String[] result = writer.decodeExecution(execution, fields, fields);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS, result.length);
		assertEquals("2", result[0]);
		assertTrue(!result[1].isEmpty());
		assertNull(result[2]);
	}
	
	@Test
	public void testDecodeExecutionWithoutUserWithUserFields() {
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		List<Field> fields = Lists.newArrayList();
		Field field = new Field();
		List<Field> fields2 = Lists.newArrayList(field);
		
		String[] result = writer.decodeExecution(execution, fields, fields2);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields2.size(), result.length);
		assertNull(result[2]);
		assertNull(result[FileWriter.NUM_STATIC_EXECUTION_FIELDS]);
	}
	
	@Test
	public void testDecodeExecutionWithEmptyUser() {
		ProjectUser user = new ProjectUser();
		user.setId(3);
		user.setContents("{}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{}");
		execution.setProjectUserId(user.getId());
		List<Field> fields = Lists.newArrayList();
		Mockito.when(userService.getProjectUser(3)).thenReturn(user);
		
		String[] result = writer.decodeExecution(execution, fields, fields);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS, result.length);
		assertEquals("3", result[2]);
	}
	
	@Test
	public void testDecodeExecutionWithFields() {
		ProjectUser user = new ProjectUser();
		user.setId(3);
		user.setContents("{\"field\":\"string2\"}");
		Execution execution = new Execution();
		execution.setId(2);
		execution.setContents("{\"field\":\"string1\"}");
		execution.setProjectUserId(user.getId());
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		Mockito.when(userService.getProjectUser(3)).thenReturn(user);
		
		String[] result = writer.decodeExecution(execution, fields, fields);
		
		assertEquals(FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields.size() + fields.size(), 
				result.length);
		assertEquals("string1", result[FileWriter.NUM_STATIC_EXECUTION_FIELDS]);
		assertEquals("string2", result[FileWriter.NUM_STATIC_EXECUTION_FIELDS + fields.size()]);
	}
	
	@Test
	public void testDecodeAddsStringFields() {
		String contents = "{\"field\":\"string\"}";
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(contents, fields);
		
		assertEquals(1, result.length);
		assertEquals("string", result[0]);
	}
	
	@Test
	public void testDecodeAddsIntegerFields() {
		String contents = "{\"field\":1}";
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.INTEGER);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(contents, fields);
		
		assertEquals(1, result.length);
		assertEquals("1", result[0]);
	}
	
	@Test
	public void testDecodeAddsDoubleFields() {
		String contents = "{\"field\":1.3}";
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.DOUBLE);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(contents, fields);
		
		assertEquals(1, result.length);
		assertEquals("1.3", result[0]);
	}
	
	@Test
	public void testDecodeAddsMultivaluateStringFields() {
		String contents = "{\"field\":[\"a\",\"b\"]}";
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.MULTIVALUATE_STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(contents, fields);
		
		assertEquals(1, result.length);
		assertEquals("a,b,", result[0]);
	}
	
	@Test
	public void testDecodeAddsBooleanFields() {
		String contents = "{\"field\":true}";
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.BOOL);
		List<Field> fields = Lists.newArrayList(field);
		
		String[] result = writer.decode(contents, fields);
		
		assertEquals(1, result.length);
		assertEquals("true", result[0]);
	}
	
	@Test
	public void testDecodeAddsSeveralFields() {
		String contents = "{\"field\":[\"a\",\"b\"],\"field2\":true,\"field3\":1}";
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
		
		String[] result = writer.decode(contents, fields);
		
		assertEquals(3, result.length);
		assertEquals("a,b,", result[0]);
		assertEquals("true", result[1]);
		assertEquals("1", result[2]);
	}
}
