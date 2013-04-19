package com.crowdplatform.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import au.com.bytecode.opencsv.CSVWriter;

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Task;
import com.google.common.collect.ObjectArrays;

public class FileWriter {
	
	private static final int NUM_STATIC_TASK_FIELDS = 1;
	private static final int NUM_STATIC_EXECUTION_FIELDS = 3;
	
	public String writeTasksExecutions(List<Task> tasks, List<Field> taskFields, List<Field> executionFields) throws IOException {
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);
		
		writeHeaders(csvWriter, taskFields, executionFields);
		
		for (Task task : tasks) {
			writeTaskExecutions(csvWriter, task, taskFields, executionFields);
		}

		String result = writer.toString();
		
		csvWriter.close();
		
		return result;
	}
	
	private void writeHeaders(CSVWriter csvWriter, List<Field> taskFields, List<Field> executionFields) {
		String[] taskHeaders = new String[NUM_STATIC_TASK_FIELDS + taskFields.size()];
		taskHeaders[0] = "task_id";
		for (int i = 0; i < taskFields.size(); ++i) {
			taskHeaders[NUM_STATIC_TASK_FIELDS + i] = taskFields.get(i).getName();
		}
		
		String[] executionHeaders = new String[NUM_STATIC_EXECUTION_FIELDS + executionFields.size()];
		executionHeaders[0] = "execution_id";
		executionHeaders[1] = "date";
		executionHeaders[2] = "userId";
		for (int i = 0; i < executionFields.size(); ++i) {
			executionHeaders[NUM_STATIC_EXECUTION_FIELDS + i] = executionFields.get(i).getName();
		}
		csvWriter.writeNext(ObjectArrays.concat(taskHeaders, executionHeaders, String.class));
	}
	
	private void writeTaskExecutions(CSVWriter csvWriter, Task task, List<Field> taskFields, List<Field> executionFields) throws IOException {
		String[] taskValues = decodeTask(task, taskFields);
		for (Execution execution : task.getExecutions()) {
			String[] executionValues = decodeExecution(execution, executionFields);
			csvWriter.writeNext(ObjectArrays.concat(taskValues, executionValues, String.class));
		}
	}
	
	private String[] decodeTask(Task task, List<Field> fields) {
		String[] values = new String[NUM_STATIC_TASK_FIELDS + fields.size()];
		values[0] = String.valueOf(task.getId());
		try {
			JsonNode node = new ObjectMapper().readTree(task.getContents());
			for (int i = 0; i < fields.size(); ++i) {
				JsonNode fieldNode = node.get(fields.get(i).getName());
				switch (fields.get(i).getType()) {
				case STRING:
					values[NUM_STATIC_TASK_FIELDS + i] = fieldNode.getTextValue();
					break;
				case INTEGER:
					values[NUM_STATIC_TASK_FIELDS + i] = String.valueOf(fieldNode.getIntValue());
					break;
				case DOUBLE:
					values[NUM_STATIC_TASK_FIELDS + i] = String.valueOf(fieldNode.getDoubleValue());
					break;
				case MULTIVALUATE_STRING:
					ArrayNode array = (ArrayNode)fieldNode;
					Iterator<JsonNode> iterator = array.getElements();
					values[NUM_STATIC_TASK_FIELDS + i] = "";
					while (iterator.hasNext()) {
						values[NUM_STATIC_TASK_FIELDS + i] += iterator.next().getTextValue() + ",";
					}
					break;
				case BOOL:
					break;
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return values;
	}
	
	private String[] decodeExecution(Execution execution, List<Field> fields) {
		String[] values = new String[NUM_STATIC_EXECUTION_FIELDS + fields.size()];
		values[0] = String.valueOf(execution.getId());
		values[1] = execution.getDate().toString();
		if (execution.getProjectUser() != null) {
			values[2] = String.valueOf(execution.getProjectUser().getId());
		}
		
		try {
			JsonNode node = new ObjectMapper().readTree(execution.getContents());
			for (int i = 0; i < fields.size(); ++i) {
				JsonNode fieldNode = node.get(fields.get(i).getName());
				switch (fields.get(i).getType()) {
				case STRING:
					values[NUM_STATIC_EXECUTION_FIELDS + i] = fieldNode.getTextValue();
					break;
				case INTEGER:
					values[NUM_STATIC_EXECUTION_FIELDS + i] = String.valueOf(fieldNode.getIntValue());
					break;
				case DOUBLE:
					values[NUM_STATIC_EXECUTION_FIELDS + i] = String.valueOf(fieldNode.getDoubleValue());
					break;
				case MULTIVALUATE_STRING:
					ArrayNode array = (ArrayNode)fieldNode;
					Iterator<JsonNode> iterator = array.getElements();
					values[NUM_STATIC_EXECUTION_FIELDS + i] = "";
					while (iterator.hasNext()) {
						values[NUM_STATIC_EXECUTION_FIELDS + i] += iterator.next().getTextValue() + ",";
					}
					break;
				case BOOL:
					break;
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return values;
	}
}
