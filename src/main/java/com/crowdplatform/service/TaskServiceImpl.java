package com.crowdplatform.service;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Task;

@Service
public class TaskServiceImpl implements TaskService {

	@PersistenceContext
	private EntityManager em;
	
    @Transactional
    public void saveTask(Task task) {
    	em.merge(task);
    }
	
    @Transactional
    public void removeTask(Integer id) {
    	Task task = em.find(Task.class, id);
        if (null != task) {
            em.remove(task);
        }
    }

	@Transactional
	public Task getTask(Long projectId, Integer taskId) {
		Query query = em.createNativeQuery("SELECT count(*) FROM project_batch pb, batch_task bt WHERE pb.project_id=:project AND pb.batches_id=bt.batch_id AND bt.tasks_id=:task");
		query.setParameter("project", projectId);
		query.setParameter("task", taskId);
		BigInteger result = (BigInteger) query.getSingleResult();
		if (result.equals(BigInteger.ONE)) {
			Task task = em.find(Task.class, taskId);
			task.getExecutions().size();
			return task;
		}
		return null;
	}
}
