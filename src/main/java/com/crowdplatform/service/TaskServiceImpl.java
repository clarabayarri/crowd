package com.crowdplatform.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Task;
import com.google.common.collect.Sets;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private BatchService batchService;
	
	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public void addTask(Task task) {
		em.persist(task);
	}
	
	@Transactional
	public Task getTask(Integer id) {
		return em.find(Task.class, id);
	}
    
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Task> listTasks() {
		Query query = em.createQuery("FROM Task");
        return query.getResultList();
	}
	
    @Transactional
    public void removeTask(Integer id) {
    	Task task = em.find(Task.class, id);
        if (null != task) {
            em.remove(task);
        }
    }
	
    @Transactional
    public void saveTask(Task task) {
    	em.merge(task);
    }
    
    @Transactional
    public void createTasks(Batch batch, Set<Field> fields, List<Map<String, String>> fileContents) {
    	Set<Task> result = Sets.newHashSet();
    	ObjectMapper mapper = new ObjectMapper();
    	
    	for (Map<String, String> line : fileContents) {
    		ObjectNode contents = mapper.createObjectNode();
    		for (Field field : fields) {
    			String value = line.get(field.getName());
    			if (value == null && field.getType() != Field.Type.MULTIVALUATE_STRING) {
    				contents.putNull(field.getName());
    			} else {
    				switch (field.getType()) {
        			case STRING:
        				contents.put(field.getName(), value);
        				break;
        			case INTEGER:
        				contents.put(field.getName(), Integer.valueOf(value));
        				break;
        			case DOUBLE:
        				contents.put(field.getName(), Float.valueOf(value));
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
    		
    		Task task = new Task();
    		task.setContents(contents.toString());
    		task.setBatch(batch);
    		addTask(task);
    		result.add(task);
    	}
    	
    	batch.setTasks(result);
		batchService.saveBatch(batch);
    }
}
