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

	public String writeExecutions(List<Execution> executions, List<Field> fields) throws IOException {
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);
		
		String[] headers = new String[2 + fields.size()];
		headers[0] = "id";
		headers[1] = "date";
		for (int i = 0; i < fields.size(); ++i) {
			headers[2 + i] = fields.get(i).getName();
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
		String[] values = new String[2 + fields.size()];
		values[0] = String.valueOf(execution.getId());
		values[1] = execution.getDate().toString();
		
		try {
			JsonNode node = new ObjectMapper().readTree(execution.getContents());
			for (int i = 0; i < fields.size(); ++i) {
				JsonNode fieldNode = node.get(fields.get(i).getName());
				switch (fields.get(i).getType()) {
				case STRING:
					values[2 + i] = fieldNode.getTextValue();
					break;
				case INTEGER:
					values[2 + i] = String.valueOf(fieldNode.getIntValue());
					break;
				case DOUBLE:
					values[2 + i] = String.valueOf(fieldNode.getDoubleValue());
					break;
				case MULTIVALUATE_STRING:
					ArrayNode array = (ArrayNode)fieldNode;
					Iterator<JsonNode> iterator = array.getElements();
					values[2 + i] = "";
					while (iterator.hasNext()) {
						values[2 + i] += iterator.next().getTextValue() + ",";
					}
					break;
				case BOOL:
					break;
				}
			}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return values;
	}
}
