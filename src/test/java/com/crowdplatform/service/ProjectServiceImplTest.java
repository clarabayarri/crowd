package com.crowdplatform.service;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.Project;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceImplTest {

	@InjectMocks
	private ProjectServiceImpl service = new ProjectServiceImpl();
	
	@Mock
	private EntityManager em;
	
	private Project project = new Project();
	private static final Integer projectId = 1;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    Mockito.when(em.find(Project.class,	projectId)).thenReturn(project);
	}
	
	@Test
	public void testAddProject() {
		service.addProject(project);
		
		Mockito.verify(em).persist(project);
	}
	
	@Test
	public void testSaveProject() {
		service.saveProject(project);
		
		Mockito.verify(em).merge(project);
	}
	
	@Test
	public void testRemoveProject() {
		service.removeProject(projectId);
		
		Mockito.verify(em).remove(project);
	}
	
	@Test
	public void testGetProject() {
		service.getProject(projectId);
		
		Mockito.verify(em).find(Project.class, projectId);
	}
}
