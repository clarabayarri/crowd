package com.crowdplatform.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Task;
import com.crowdplatform.util.FileReader;
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
	
	@Transactional
	public Task getRandomTask() {
		Query query = em.createQuery("FROM Task");
		int total = query.getResultList().size();
		int index = (new Random()).nextInt(total);
		return ((Task) query.getResultList().get(index));
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
    public void createTasks(Batch batch, MultipartFile taskFile) throws IOException {
    	FileReader reader = new FileReader();
    	List<Map<String, String>> fileContents = reader.readCSVFile(taskFile);
    	
    	Set<Task> result = Sets.newHashSet();
    	ObjectMapper mapper = new ObjectMapper();
    	
    	for (Map<String, String> line : fileContents) {
    		String encoding = mapper.writeValueAsString(line);
    		Task task = new Task();
    		task.setContents(encoding);
    		task.setBatch(batch);
    		addTask(task);
    		result.add(task);
    	}
    	
    	batch.setTasks(result);
		batchService.saveBatch(batch);
    }
}
