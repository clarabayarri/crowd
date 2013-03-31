package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

	@InjectMocks
	private ProjectController controller = new ProjectController();
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private SecurityContext context;
	
	private static final String username = "username";
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    GrantedAuthority authority = new GrantedAuthorityImpl("ROLE_PLATFORM_USER");
	    List<GrantedAuthority> grantedAuthorities = Lists.newArrayList(authority);
	    UserDetails userDetails = 
	    		new org.springframework.security.core.userdetails.User(username, "123456", true, false, false, false, grantedAuthorities);
	    Authentication authentication = new TestingAuthenticationToken(userDetails, "password", grantedAuthorities);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	
	@Test
	public void testListProjectsHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		Mockito.when(userService.getUser(username)).thenReturn(new PlatformUser());
		
		String result = controller.listProjects(model, null);
		
		assertEquals("projects", result);
	}
	
	@Test
	public void testListProjectsRetrievesProjectsToModel() {
		Set<Project> projects = Sets.newHashSet(new Project());
		PlatformUser user = new PlatformUser();
		user.setProjects(projects);
		Mockito.when(userService.getUser(username)).thenReturn(user);
		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn(username);
		Mockito.when(context.getAuthentication()).thenReturn(auth);
		Model model = Mockito.mock(Model.class);
		
		controller.listProjects(model, null);
		
		Mockito.verify(userService).getUser(username);
		Mockito.verify(model).addAttribute(Mockito.any());
	}
	
	@Test
	public void testListProjectsAddsRegisteredParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		String username = "username";
		Mockito.when(userService.getUser(username)).thenReturn(new PlatformUser());
		
		controller.listProjects(model, true);
		
		Mockito.verify(model).addAttribute("registered", true);
	}
	
	@Test
	public void testGetProjectHandleRequestView() {
		PlatformUser user = new PlatformUser();
		Mockito.when(userService.getUser(Mockito.anyString())).thenReturn(user);
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
