package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.crowdplatform.model.Project;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

	@InjectMocks
	private ProjectController controller = new ProjectController();
	
	@Mock
	private ProjectService service;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}

	
	@Test
	public void testListProjectsHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.listProjects(model);
		
		assertEquals("projects", result);
	}
	
	@Test
	public void testListProjectsRetrievesProjectsToModel() {
		List<Project> projects = Lists.newArrayList(new Project(), new Project());
		Mockito.when(service.listProjects()).thenReturn(projects);
		Model model = Mockito.mock(Model.class);
		
		controller.listProjects(model);
		
		Mockito.verify(service).listProjects();
		Mockito.verify(model).addAttribute(projects);
	}
	
	@Test
	public void testGetProjectHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getProject(1, model);
		
		assertEquals("project", result);
	}
	
	@Test
	public void testGetProjectRetrievesProjectToModel() {
		Project project = new Project();
		Mockito.when(service.getProject(1)).thenReturn(project);
		Model model = Mockito.mock(Model.class);
		
		controller.getProject(1, model);
		
		Mockito.verify(service).getProject(1);
		Mockito.verify(model).addAttribute(project);
	}
}
