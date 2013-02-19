package com.example.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Batch;

@Service
public class BatchServiceImpl implements BatchService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public void addBatch(Batch batch) {
		em.persist(batch);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
    public List<Batch> listBatches() {
		Query query = em.createQuery("FROM Batch");
        return query.getResultList();
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
    	System.out.println(batch.getTasks().size());
    	System.out.println(batch.getNumTasks());
    	return batch;
    }
    
    @Transactional
    public void saveBatch(Batch batch) {
    	em.merge(batch);
    }
    
}
