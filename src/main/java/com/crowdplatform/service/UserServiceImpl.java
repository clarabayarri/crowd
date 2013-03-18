package com.crowdplatform.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.User;

@Service
public class UserServiceImpl implements UserService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public boolean usernameExists(String username) {
		User user = em.find(User.class, username);
		return user !=  null;
	}

	@Transactional
	public void addUser(User user) {
		em.persist(user);
	}

	

}
