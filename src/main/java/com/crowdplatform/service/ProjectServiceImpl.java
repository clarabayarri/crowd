package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	private EntityManager em;
	 
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
	        this.em = entityManager;
	}
	
	@Transactional
	public Project getProject(Integer id) {
		Project project = em.find(Project.class, id);
		project.getBatches().size();
		return project;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Project> listProjects() {
		Query query = em.createQuery("FROM Project");
        return query.getResultList();
	}

	@Transactional
	public void addProject(Project project) {
		em.persist(project);
	}

	@Transactional
	public void removeProject(Integer id) {
		Project project = em.find(Project.class, id);
		if (null != project) {
			em.remove(project);
		}
	}

	@Transactional
	public void saveProject(Project project) {
		em.merge(project);
	}

}
