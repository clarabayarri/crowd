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

public class FileWriter {
	
	private static final int NUM_STATIC_FIELDS = 3;

	public String writeExecutions(List<Execution> executions, List<Field> fields) throws IOException {
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);
		
		String[] headers = new String[NUM_STATIC_FIELDS + fields.size()];
		headers[0] = "id";
		headers[1] = "date";
		headers[2] = "userId";
		for (int i = 0; i < fields.size(); ++i) {
			headers[NUM_STATIC_FIELDS + i] = fields.get(i).getName();
		}
		csvWriter.writeNext(headers);
		
		for (Execution execution : executions) {
			String[] values = decodeExecution(execution, fields);
			csvWriter.writeNext(values);
		}
		String result = writer.toString();
		
		csvWriter.close();
		
		return result;
	}
	
	private String[] decodeExecution(Execution execution, List<Field> fields) {
		String[] values = new String[NUM_STATIC_FIELDS + fields.size()];
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
					values[NUM_STATIC_FIELDS + i] = fieldNode.getTextValue();
					break;
				case INTEGER:
					values[NUM_STATIC_FIELDS + i] = String.valueOf(fieldNode.getIntValue());
					break;
				case DOUBLE:
					values[NUM_STATIC_FIELDS + i] = String.valueOf(fieldNode.getDoubleValue());
					break;
				case MULTIVALUATE_STRING:
					ArrayNode array = (ArrayNode)fieldNode;
					Iterator<JsonNode> iterator = array.getElements();
					values[NUM_STATIC_FIELDS + i] = "";
					while (iterator.hasNext()) {
						values[NUM_STATIC_FIELDS + i] += iterator.next().getTextValue() + ",";
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
