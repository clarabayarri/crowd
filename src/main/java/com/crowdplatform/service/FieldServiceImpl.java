package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Field;

@Service
public class FieldServiceImpl implements FieldService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public void addField(Field field) {
		em.persist(field);
	}
	
	@Transactional
	public void removeField(Integer id) {
		Field field = em.find(Field.class, id);
		if (field != null) {
			em.remove(field);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Field> listFields() {
		Query query = em.createQuery("FROM Field");
		return query.getResultList();
	}

}
