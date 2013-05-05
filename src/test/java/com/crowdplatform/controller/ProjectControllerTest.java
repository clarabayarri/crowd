package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

	@InjectMocks
	private ProjectController controller = new ProjectController();
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private ProjectService projectService;
	
	private static final String projectId = "1";
	private Project project = new Project();
	private PlatformUser user = new PlatformUser();
	private static final String username = "username";
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    project.setOwnerId(username);
	    user.setUsername(username);
	    project.setId(projectId);
	    List<Project> projects = Lists.newArrayList(project);
	    Mockito.when(projectService.getProjectsForUser(user.getUsername())).thenReturn(projects);
		Mockito.when(userService.getCurrentUser()).thenReturn(user);
		
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
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
	public void testListProjectsDoesntAddProjectsIfNotAuthorized() {
		Mockito.when(userService.getCurrentUser()).thenReturn(null);
		Model model = Mockito.mock(Model.class);
		
		controller.listProjects(model, null);
		
		Mockito.verifyZeroInteractions(model);
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
		
		String result = controller.getProject(projectId, model);
		
		assertEquals("project", result);
	}
	
	@Test
	public void testGetProjectRetrievesProjectToModelWhenOwnedByUser() {
		Model model = Mockito.mock(Model.class);
		
		controller.getProject(projectId, model);
		
		Mockito.verify(projectService).getProject(projectId);
		Mockito.verify(model).addAttribute(project);
	}
	
	@Test
	public void testGetProjectDoesntRetrievesProjectToModelWhenNotOwnedByUser() {
		project.setOwnerId("other username");
		Model model = Mockito.mock(Model.class);
		
		controller.getProject(projectId, model);
		
		Mockito.verifyZeroInteractions(model);
	}
	
	@Test
	public void testResetProjectUIDHandleRequestView() {
		String result = controller.resetProjectUID(projectId);
		
		String expected = "redirect:/project/" + projectId;
		assertEquals(expected, result);
		Mockito.verify(projectService).saveProject(project);
	}
	
	@Test
	public void testResetProjectUID() {
		controller.resetProjectUID(projectId);
		
		assertNotNull(project.getUid());
	}
	
	@Test
	public void testResetProjectUIDDoesntChangeUIDIfNotAuthorized() {
		project.setOwnerId("other username");
		project.setUid(new Long(1));
		
		controller.resetProjectUID(projectId);
		
		assertEquals(new Long(1), project.getUid());
	}
	
	@Test
	public void testDeleteProjectHandleRequestView() {
		String result = controller.deleteProject(projectId);
		
		assertEquals("redirect:/projects", result);
	}
	
	@Test
	public void testDeleteProject() {
		controller.deleteProject(projectId);
		
		Mockito.verify(projectService).removeProject(projectId);
	}
	
	@Test
	public void testDeleteProjectDoesNothingIfNotAuthorized() {
		project.setOwnerId("other username");
		
		controller.deleteProject(projectId);
		
		Mockito.verify(projectService, Mockito.never()).removeProject(projectId);
	}
}
