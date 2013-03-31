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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.UserService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {

	@InjectMocks
	private BatchController controller = new BatchController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private BatchService service;
	
	@Mock
	private UserService userService;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    GrantedAuthority authority = new GrantedAuthorityImpl("ROLE_PLATFORM_USER");
	    List<GrantedAuthority> grantedAuthorities = Lists.newArrayList(authority);
	    UserDetails userDetails = 
	    		new org.springframework.security.core.userdetails.User("username", "123456", true, false, false, false, grantedAuthorities);
	    Authentication authentication = new TestingAuthenticationToken(userDetails, "password", grantedAuthorities);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
	    PlatformUser user = new PlatformUser();
	    Project project = new Project();
	    project.setId(1);
	    user.addProject(project);
	    Mockito.when(userService.getUser("username")).thenReturn(user);
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getBatch(1, 1, model, null);
		
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToModel() {
		Batch batch = new Batch();
		Mockito.when(service.getBatch(1)).thenReturn(batch);
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(1, 1, model, null);
		
		Mockito.verify(model).addAttribute(batch);
		Mockito.verify(service).getBatch(1);
	}
	
	@Test
	public void testGetBatchAddsCreatedParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(1, 1, model, true);
		
		Mockito.verify(model).addAttribute("created", true);
	}
	
	@Test
	public void testStartBatchCallsService() {
		controller.startBatch(1, 1);
		
		Mockito.verify(service).startBatch(1);
	}
	
	@Test
	public void testPauseBatchCallsService() {
		controller.pauseBatch(1, 1);
		
		Mockito.verify(service).pauseBatch(1);
	}
	
	@Test
	public void testNewBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		Mockito.when(projectService.getProject(1)).thenReturn(new Project());
		
		String result = controller.newBatch(1, model);
		
		assertEquals("create", result);
	}
	
	@Test
	public void testNewBatchAddsEmptyBatchToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.newBatch(1, model);
		
		Mockito.verify(model, Mockito.times(2)).addAttribute(Mockito.any());
	}
	
	@Test
	public void testCreateBatchCallsService() {
		Batch batch = new Batch();
		Mockito.when(projectService.getProject(1)).thenReturn(new Project());
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.createBatch(batch, 1, bindingResult, null);
		
		Mockito.verify(service).createBatch(batch, 1);
	}

}
