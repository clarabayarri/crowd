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
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddProject() {
		Project project = new Project();
		
		service.addProject(project);
		
		Mockito.verify(em).persist(project);
	}
	
	@Test
	public void testRemoveProject() {
		Project project = new Project();
		Mockito.when(em.find(Project.class,	1)).thenReturn(project);
		
		service.removeProject(1);
		
		Mockito.verify(em).remove(project);
	}
	
	@Test
	public void testSaveProject() {
		Project project = new Project();
		
		service.saveProject(project);
		
		Mockito.verify(em).merge(project);
	}
}
