package com.crowdplatform.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.ProjectUser;

@Service
public class ProjectUserServiceImpl implements ProjectUserService {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void addProjectUser(ProjectUser user) {
		em.persist(user);
	}

	@Transactional
	public ProjectUser getProjectUser(Integer id) {
		ProjectUser user = em.find(ProjectUser.class, id);
		return user;
	}

	@Override
	public ProjectUser getProjectUserByUsername(String username) {
		Query query = em.createQuery("FROM ProjectUser WHERE username=:username");
		query.setParameter("username", username);
		if (query.getResultList().size() > 0) {
			ProjectUser user = (ProjectUser) query.getResultList().get(0);
			return user;
		}
		return null;
	}
	
}
