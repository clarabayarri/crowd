package com.crowdplatform.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVWriter;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;

@Service
public class CSVFileWriter implements FileWriter {

	@VisibleForTesting
	public static final int NUM_STATIC_TASK_FIELDS = 1;
	@VisibleForTesting
	public static final int NUM_STATIC_EXECUTION_FIELDS = 3;

	public String writeTasksExecutions(Project project, Batch batch, BatchExecutionCollection collection, Boolean header) throws IOException {
		StringWriter writer = new StringWriter();
		CSVWriter csvWriter = new CSVWriter(writer);

		if (header) {
			String[] headers = writeHeaders(project.getInputFields(), project.getOutputFields(), project.getUserFields());
			csvWriter.writeNext(headers);
		}

		Map<Integer, String[]> taskValues = Maps.newHashMap();
		for (Task task : batch.getTasks()) {
			taskValues.put(task.getId(), decodeTask(task, project.getInputFields()));
		}

		for (Execution execution : collection.getExecutions()) {
			ProjectUser user = null;
			if (execution.getProjectUserId() != null)
				user = project.getUser(execution.getProjectUserId());
			writeExecution(csvWriter, taskValues.get(execution.getTaskId()), execution, user, project.getOutputFields(), project.getUserFields());
		}

		String result = writer.toString();

		csvWriter.close();

		return result;
	}

	@VisibleForTesting
	public String[] writeHeaders(List<Field> taskFields, List<Field> executionFields, List<Field> userFields) {
		String[] taskHeaders = new String[NUM_STATIC_TASK_FIELDS + taskFields.size()];
		taskHeaders[0] = "task_id";
		for (int i = 0; i < taskFields.size(); ++i) {
			taskHeaders[NUM_STATIC_TASK_FIELDS + i] = taskFields.get(i).getName();
		}

		String[] executionHeaders = new String[NUM_STATIC_EXECUTION_FIELDS + executionFields.size() + userFields.size()];
		executionHeaders[0] = "execution_id";
		executionHeaders[1] = "date";
		executionHeaders[2] = "userId";
		for (int i = 0; i < executionFields.size(); ++i) {
			executionHeaders[NUM_STATIC_EXECUTION_FIELDS + i] = executionFields.get(i).getName();
		}
		for (int i = 0; i < userFields.size(); ++i) {
			executionHeaders[NUM_STATIC_EXECUTION_FIELDS + executionFields.size() + i] = userFields.get(i).getName();
		}

		return ObjectArrays.concat(taskHeaders, executionHeaders, String.class);
	}

	private void writeExecution(CSVWriter csvWriter, String[] taskValues, Execution execution, ProjectUser user, List<Field> executionFields, 
			List<Field> userFields) {
		String[] executionValues = decodeExecution(execution, user, executionFields, userFields);
		csvWriter.writeNext(ObjectArrays.concat(taskValues, executionValues, String.class));
	}

	@VisibleForTesting
	public String[] decodeTask(Task task, List<Field> fields) {
		String[] values = new String[NUM_STATIC_TASK_FIELDS];
		values[0] = String.valueOf(task.getId());

		String[] taskValues = decode(task.getContents(), fields);
		values = ObjectArrays.concat(values, taskValues, String.class);
		return values;
	}

	@VisibleForTesting
	public String[] decodeExecution(Execution execution, ProjectUser user, List<Field> fields, List<Field> userFields) {
		String[] values = new String[NUM_STATIC_EXECUTION_FIELDS];
		values[0] = String.valueOf(execution.getId());
		values[1] = execution.getDate().toString();

		String[] executionValues = decode(execution.getContents(), fields);
		values = ObjectArrays.concat(values, executionValues, String.class);

		if (user != null && user.getContents() != null) {
			values[2] = String.valueOf(execution.getProjectUserId());
			String[] userValues = decode(user.getContents(), userFields);
			values = ObjectArrays.concat(values,  userValues, String.class);			
		} else {
			String[] userValues = new String[userFields.size()];
			values = ObjectArrays.concat(values,  userValues, String.class);
		}

		return values;
	}

	@SuppressWarnings("unchecked")
	@VisibleForTesting
	public String[] decode(Map<String, Object> contents, List<Field> fields) {
		String[] values = new String[fields.size()];

		for (int i = 0; i < fields.size(); ++i) {
			Field field = fields.get(i);
			switch (fields.get(i).getType()) {
			case MULTIVALUATE_STRING:
				Collection<String> collection = (Collection<String>) contents.get(field.getName());
				Iterator<String> iterator = collection.iterator();
				values[i] = "";
				while (iterator.hasNext()) {
					values[i] += iterator.next() + ",";
				}
				break;
			default:
				Object value = contents.get(field.getName());
				if (value != null) {
					values[i] = value.toString();
				}
				break;
			}
		}

		return values;
	}
}
