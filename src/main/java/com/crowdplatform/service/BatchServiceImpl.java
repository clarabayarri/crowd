package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.Task;
import com.google.common.collect.Lists;

@Service
public class BatchServiceImpl implements BatchService {

	@Autowired
	private ProjectService projectService;
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void addBatch(Batch batch) {
		em.persist(batch);
	}
    
    @Transactional
    public void removeBatch(Integer id) {
    	Batch batch = em.find(Batch.class, id);
        if (null != batch) {
            em.remove(batch);
        }
    }
    
    @Transactional
    public Batch getBatch(Integer id) {
    	Batch batch = em.find(Batch.class, id);
    	if (batch != null) {
    		batch.getTasks().size();
        	batch.getNumTasks();
        	batch.updatePercentageComplete();
    	}
    	return batch;
    }
    
    @Transactional
    public List<Integer> listRunningBatchIds(Integer projectId) {
    	Project project = projectService.getProject(projectId);
    	List<Integer> result = Lists.newArrayList();
    	if (project != null) {
    		for (Batch batch : project.getBatches()) {
        		if (batch.getState() == Batch.State.RUNNING) {
        			result.add(batch.getId());
        		}
        	}
    	}
    	return result;
    }
    
    @Transactional
    public List<Integer> listCompletedBatchIds(Integer projectId) {
    	Project project = projectService.getProject(projectId);
    	List<Integer> result = Lists.newArrayList();
    	if (project != null) {
    		for (Batch batch : project.getBatches()) {
        		if (batch.getState() == Batch.State.COMPLETE) {
        			result.add(batch.getId());
        		}
        	}
    	}
    	return result;
    }
    
    @Transactional
    public void startBatch(Integer id) {
    	Batch batch = em.find(Batch.class, id);
    	batch.setState(Batch.State.RUNNING);
    	em.merge(batch);
    }
    
    @Transactional
    public void pauseBatch(Integer id) {
    	Batch batch = em.find(Batch.class, id);
    	batch.setState(Batch.State.PAUSED);
    	em.merge(batch);
    }
    
    @Transactional
    public List<Execution> listExecutions(Integer id) {
    	Batch batch = em.find(Batch.class, id);
    	List<Execution> results = Lists.newArrayList();
    	for (Task task : batch.getTasks()) {
    		results.addAll(task.getExecutions());
    	}
    	return results;
    }
    
}
