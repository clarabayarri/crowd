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
import com.crowdplatform.service.PlatformUserService;
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
	private PlatformUserService userService;
	
	private Batch batch = new Batch();
	private Project project = new Project();
	private static final Integer projectId = 1;
	private static final Integer batchId = 2;
	
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
	    project.setId(projectId);
	    batch.setId(batchId);
	    project.addBatch(batch);
	    user.addProject(project);
	    Mockito.when(userService.getUser("username")).thenReturn(user);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
		Mockito.when(service.getBatch(batchId)).thenReturn(batch);
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getBatch(projectId, batchId, model, null);
		
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batchId, model, null);
		
		Mockito.verify(model).addAttribute(batch);
		Mockito.verify(service).getBatch(batchId);
	}
	
	@Test
	public void testGetBatchAddsCreatedParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batchId, model, true);
		
		Mockito.verify(model).addAttribute("created", true);
	}
	
	@Test
	public void testStartBatchCallsService() {
		controller.startBatch(projectId, batchId);
		
		Mockito.verify(service).startBatch(batchId);
	}
	
	@Test
	public void testPauseBatchCallsService() {
		controller.pauseBatch(projectId, batchId);
		
		Mockito.verify(service).pauseBatch(batchId);
	}
	
	@Test
	public void testNewBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.newBatch(projectId, model);
		
		assertEquals("create", result);
	}
	
	@Test
	public void testNewBatchAddsEmptyBatchToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.newBatch(projectId, model);
		
		Mockito.verify(model, Mockito.times(2)).addAttribute(Mockito.any());
	}
	
	@Test
	public void testCreateBatchCallsService() {
		Project project = Mockito.mock(Project.class);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.createBatch(batch, 1, bindingResult, null);
		
		Mockito.verify(project).addBatch(batch);
		Mockito.verify(projectService).saveProject(project);
	}

}
