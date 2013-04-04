package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	
	private static final Long projectId = new Long(1);
	private Project project = new Project();
	private PlatformUser user = new PlatformUser();
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    Mockito.when(userService.currentUserIsAuthorizedForProject(projectId)).thenReturn(true);
	    project.setId(projectId);
	    user.setProjects(Sets.newHashSet(project));
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
		Mockito.when(userService.currentUserIsAuthorizedForProject(projectId)).thenReturn(false);
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
		Mockito.when(userService.currentUserIsAuthorizedForProject(projectId)).thenReturn(false);
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
		
		assertEquals(0, user.getProjects().size());
		Mockito.verify(userService).saveUser(user);
		Mockito.verify(projectService).removeProject(projectId);
	}
	
	@Test
	public void testDeleteProjectDoesNothingIfNotAuthorized() {
		Mockito.when(userService.currentUserIsAuthorizedForProject(projectId)).thenReturn(false);
		
		controller.deleteProject(projectId);
		
		assertEquals(1, user.getProjects().size());
		Mockito.verifyZeroInteractions(projectService);
	}
}
