package com.crowdplatform.service;

import java.security.SecureRandom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.PasswordResetRequest;

@Service
public class PasswordResetRequestServiceImpl implements PasswordResetRequestService {

	private EntityManager em;
	
	private SecureRandom secureRandom = new SecureRandom();
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public void addRequest(PasswordResetRequest request) {
		Query query = em.createQuery("FROM PasswordResetRequest WHERE user_username='" + request.getUser().getUsername() + "'");
		for (Object item : query.getResultList()) {
			em.remove(item);
		}
		Long id = secureRandom.nextLong();
		request.setId(id);
		em.persist(request);
	}

	@Transactional
	public PasswordResetRequest getRequest(Long id) {
		return em.find(PasswordResetRequest.class, id);
	}

	@Transactional
	public void removeRequest(PasswordResetRequest request) {
		em.remove(request);
	}

}