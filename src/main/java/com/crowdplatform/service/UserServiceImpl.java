package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

	@Transactional
	public User getUser(String username) {
		User user = em.find(User.class, username);
		user.getProjects().size();
		return user;
	}

	@Transactional
	public void saveUser(User user) {
		em.merge(user);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<User> listUsers() {
		Query query = em.createQuery("FROM User");
		return query.getResultList();
	}
	
	@Transactional
	public void removeUser(String username) {
		User user = em.find(User.class, username);
		if (user != null) {
			em.remove(user);
		}
	}

}
