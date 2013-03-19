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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import com.crowdplatform.model.Project;
import com.crowdplatform.model.User;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.UserService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

	@InjectMocks
	private ProjectController controller = new ProjectController();
	
	@Mock
	private UserService userService;
	
	@Mock
	private ProjectService service;
	
	@Mock
	private SecurityContext context;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    GrantedAuthority authority = new GrantedAuthorityImpl("ROLE_PLATFORM_USER");
	    List<GrantedAuthority> grantedAuthorities = Lists.newArrayList(authority);
	    UserDetails userDetails = 
	    		new org.springframework.security.core.userdetails.User("username", "123456", true, false, false, false, grantedAuthorities);
	    Authentication authentication = new TestingAuthenticationToken(userDetails, "password", grantedAuthorities);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	
	@Test
	public void testListProjectsHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		String username = "username";
		Mockito.when(userService.getUser(username)).thenReturn(new User());
		
		String result = controller.listProjects(model);
		
		assertEquals("projects", result);
	}
	
	@Test
	public void testListProjectsRetrievesProjectsToModel() {
		List<Project> projects = Lists.newArrayList(new Project(), new Project());
		Mockito.when(service.listProjects()).thenReturn(projects);
		String username = "username";
		Mockito.when(userService.getUser(username)).thenReturn(new User());
		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn(username);
		Mockito.when(context.getAuthentication()).thenReturn(auth);
		Model model = Mockito.mock(Model.class);
		
		controller.listProjects(model);
		
		Mockito.verify(userService).getUser(username);
		Mockito.verify(model).addAttribute(Mockito.any());
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
