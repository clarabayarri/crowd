package com.example.service;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Task;

@Service
public class TaskServiceImpl implements TaskService {

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
}
