package com.example.service;

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
	public Task getTask() {
		Query query = em.createQuery("FROM Task");
		return ((Task) query.getResultList().get(0));
	}
	
}
