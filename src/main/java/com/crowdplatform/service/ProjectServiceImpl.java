package com.crowdplatform.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void addProject(Project project) {
		em.persist(project);
	}

	@Transactional
	public void saveProject(Project project) {
		em.merge(project);
	}

	@Transactional
	public void removeProject(Integer id) {
		Project project = em.find(Project.class, id);
		if (null != project) {
			em.remove(project);
		}
	}
	
	@Transactional
	public Project getProject(Integer id) {
		Project project = em.find(Project.class, id);
		for (Batch batch : project.getBatches()) {
			batch.updatePercentageComplete();
		}
		return project;
	}

}
