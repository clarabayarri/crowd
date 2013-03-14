package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Execution;

@Service
public class ExecutionServiceImpl implements ExecutionService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	    this.em = entityManager;
	}
	
	@Transactional
	public void addExecution(Execution execution) {
		em.persist(execution);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Execution> listExecutions() {
		Query query = em.createQuery("FROM Execution");
        return query.getResultList();
	}

	@Transactional
	public void removeExecution(Integer id) {
		Execution execution = em.find(Execution.class, id);
		if (null != execution) {
			em.remove(execution);
		}
	}

}
