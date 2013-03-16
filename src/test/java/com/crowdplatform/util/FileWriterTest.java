package com.crowdplatform.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.google.common.collect.Lists;

public class FileWriterTest {

	private FileWriter writer = new FileWriter();

	@Test
	public void testWriteExecutionsReturnsString() throws IOException {
		List<Execution> executions = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeExecutions(executions, fields);
		
		assertNotNull(result);
	}
	
	@Test
	public void testWriteExecutionsReturnsHeaderRow() throws IOException {
		List<Execution> executions = Lists.newArrayList();
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = "\"id\",\"date\"\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void testWriteExecutionsReturnsHeaderRowWithFields() throws IOException {
		List<Execution> executions = Lists.newArrayList();
		Field field = new Field();
		field.setName("field");
		List<Field> fields = Lists.newArrayList(field);
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = "\"id\",\"date\",\"field\"\n";
		assertEquals(expected, result);
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionBasicInformation() throws IOException {
		Execution execution = new Execution();
		execution.setId(1);
		execution.setContents("{}");
		List<Execution> executions = Lists.newArrayList(execution);
		List<Field> fields = Lists.newArrayList();
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = "\"id\",\"date\"\n\"1\",";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionFields() throws IOException {
		Execution execution = new Execution();
		execution.setId(1);
		execution.setContents("{\"field\":\"contents\"}");
		List<Execution> executions = Lists.newArrayList(execution);
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = ",\"contents\"\n";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionIntegerFields() throws IOException {
		Execution execution = new Execution();
		execution.setId(1);
		execution.setContents("{\"field\":1}");
		List<Execution> executions = Lists.newArrayList(execution);
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.INTEGER);
		List<Field> fields = Lists.newArrayList(field);
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = ",\"1\"\n";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionDoubleFields() throws IOException {
		Execution execution = new Execution();
		execution.setId(1);
		execution.setContents("{\"field\":1.07}");
		List<Execution> executions = Lists.newArrayList(execution);
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.DOUBLE);
		List<Field> fields = Lists.newArrayList(field);
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = ",\"1.07\"\n";
		assertTrue(result.contains(expected));
	}
	
	@Test
	public void testWriteExecutionsAddsExecutionMultivaluateFields() throws IOException {
		Execution execution = new Execution();
		execution.setId(1);
		execution.setContents("{\"field\":[\"a\",\"b\"]}");
		List<Execution> executions = Lists.newArrayList(execution);
		Field field = new Field();
		field.setName("field");
		field.setType(Field.Type.MULTIVALUATE_STRING);
		List<Field> fields = Lists.newArrayList(field);
		
		String result = writer.writeExecutions(executions, fields);
		
		String expected = ",\"a,b,\"\n";
		assertTrue(result.contains(expected));
	}
}
