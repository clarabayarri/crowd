package com.crowdplatform.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Task;

@Service
public class TaskCreator {

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Create a set of tasks and add them to the provided Batch.
	 * @param batch Batch that will contain the tasks
	 * @param fields Fields that should be considered for the Task contents
	 * @param fileContents Data used to create the tasks
	 */
	@Transactional
    public void createTasks(Batch batch, Set<Field> fields, List<Map<String, String>> fileContents) {
    	for (Map<String, String> line : fileContents) {
    		ObjectNode contents = encodeLine(fields, line);
    		
    		Task task = new Task();
    		task.setContents(contents.toString());
    		task.setBatch(batch);
    		batch.addTask(task);
    	}
    }
    
    private ObjectNode encodeLine(Set<Field> fields, Map<String, String> line) {
    	ObjectNode contents = mapper.createObjectNode();
		for (Field field : fields) {
			String value = line.get(field.getName());
			if (value == null && field.getType() != Field.Type.MULTIVALUATE_STRING) {
				contents.putNull(field.getName());
			} else{
				switch (field.getType()) {
    			case STRING:
    				contents.put(field.getName(), value);
    				break;
    			case INTEGER:
    				if (!value.isEmpty()) {
    					contents.put(field.getName(), Integer.valueOf(value));
    				}
    				break;
    			case DOUBLE:
    				if (!value.isEmpty()) {
    					contents.put(field.getName(), Float.valueOf(value));
    				}
    				break;
    			case MULTIVALUATE_STRING:
    				ArrayNode array = mapper.createArrayNode();
    				for (String column : field.getColumnNames()) {
    					if (line.get(column) != null && !line.get(column).isEmpty())
    						array.add(line.get(column));
    				}
    				contents.put(field.getName(), array);
    				break;
    			case BOOL:
    				break;
    			} 
			}
		}
		return contents;
    }
}
