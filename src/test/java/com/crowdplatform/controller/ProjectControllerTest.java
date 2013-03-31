package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

	@InjectMocks
	private ProjectController controller = new ProjectController();
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private ProjectService projectService;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    Mockito.when(userService.currentUserIsAuthorizedForProject(Mockito.anyInt())).thenReturn(true);
	    Set<Project> projects = Sets.newHashSet(new Project());
		PlatformUser user = new PlatformUser();
		user.setProjects(projects);
		Mockito.when(userService.getCurrentUser()).thenReturn(user);
	}

	
	@Test
	public void testListProjectsHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.listProjects(model, null);
		
		assertEquals("projects", result);
	}
	
	@Test
	public void testListProjectsRetrievesProjectsToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.listProjects(model, null);
		
		Mockito.verify(model).addAttribute(Mockito.any());
	}
	
	@Test
	public void testListProjectsAddsRegisteredParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.listProjects(model, true);
		
		Mockito.verify(model).addAttribute("registered", true);
	}
	
	@Test
	public void testGetProjectHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getProject(1, model);
		
		assertEquals("project", result);
	}
	
	@Test
	public void testGetProjectRetrievesProjectToModelWhenOwnedByUser() {
		Project project = new Project();
		Mockito.when(projectService.getProject(1)).thenReturn(project);
		Mockito.when(userService.currentUserIsAuthorizedForProject(1)).thenReturn(true);
		Model model = Mockito.mock(Model.class);
		
		controller.getProject(1, model);
		
		Mockito.verify(projectService).getProject(1);
		Mockito.verify(model).addAttribute(project);
	}
	
	@Test
	public void testGetProjectDoesntRetrievesProjectToModelWhenNotOwnedByUser() {
		Project project = new Project();
		Mockito.when(projectService.getProject(1)).thenReturn(project);
		Mockito.when(userService.currentUserIsAuthorizedForProject(1)).thenReturn(false);
		Model model = Mockito.mock(Model.class);
		
		controller.getProject(1, model);
		
		Mockito.verifyZeroInteractions(model);
	}
}
