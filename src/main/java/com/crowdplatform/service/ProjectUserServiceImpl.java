package com.crowdplatform.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.ProjectUser;

@Service
public class ProjectUserServiceImpl implements ProjectUserService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public void addProjectUser(ProjectUser user) {
		em.persist(user);
	}

	@Transactional
	public ProjectUser getProjectUser(String username) {
		ProjectUser user = em.find(ProjectUser.class, username);
		return user;
	}
	
}
