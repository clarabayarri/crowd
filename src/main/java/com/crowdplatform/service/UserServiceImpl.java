package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.PlatformUser;

@Service
public class UserServiceImpl implements UserService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public boolean usernameExists(String username) {
		PlatformUser user = em.find(PlatformUser.class, username);
		return user !=  null;
	}

	@Transactional
	public void addUser(PlatformUser user) {
		em.persist(user);
	}

	@Transactional
	public PlatformUser getUser(String username) {
		PlatformUser user = em.find(PlatformUser.class, username);
		user.getProjects().size();
		return user;
	}

	@Transactional
	public void saveUser(PlatformUser user) {
		em.merge(user);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<PlatformUser> listUsers() {
		Query query = em.createQuery("FROM User");
		return query.getResultList();
	}
	
	@Transactional
	public void removeUser(String username) {
		PlatformUser user = em.find(PlatformUser.class, username);
		if (user != null) {
			em.remove(user);
		}
	}

}
