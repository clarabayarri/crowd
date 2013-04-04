package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {

	@InjectMocks
	private BatchController controller = new BatchController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private BatchService batchService;
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private TaskService taskService;
	
	private Batch batch = new Batch();
	private Project project = new Project();
	private static final Long projectId = new Long(1);
	private static final Integer batchId = 2;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    PlatformUser user = new PlatformUser();
	    project.setId(projectId);
	    batch.setId(batchId);
	    project.addBatch(batch);
	    user.addProject(project);
	    Mockito.when(userService.getUser("username")).thenReturn(user);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
		Mockito.when(batchService.getBatch(batchId)).thenReturn(batch);
		
		Mockito.when(userService.currentUserIsAuthorizedForProject(projectId)).thenReturn(true);
		Mockito.when(userService.currentUserIsAuthorizedForBatch(projectId, batchId)).thenReturn(true);
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getBatch(projectId, batchId, model, null);
		
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToModelIfAuthorized() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batchId, model, null);
		
		Mockito.verify(model).addAttribute(batch);
		Mockito.verify(projectService).getProject(projectId);
		Mockito.verify(batchService).getBatch(batchId);
	}
	
	@Test
	public void testGetBatchDoesntRetrievesBatchToModelIfNotAuthorized() {
		Model model = Mockito.mock(Model.class);
		Mockito.when(userService.currentUserIsAuthorizedForBatch(projectId, batchId)).thenReturn(false);
		
		controller.getBatch(projectId, batchId, model, null);
		
		Mockito.verifyZeroInteractions(model);
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verifyZeroInteractions(projectService);
	}
	
	@Test
	public void testGetBatchAddsCreatedParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batchId, model, true);
		
		Mockito.verify(model).addAttribute("created", true);
	}
	
	@Test
	public void testStartBatchHandleRequestView() {
		String result = controller.startBatch(projectId, batchId);
		
		String expected = "redirect:/project/" + projectId + "/batch/" + batchId;
		assertEquals(expected, result);
	}
	
	@Test
	public void testStartBatchCallsService() {
		controller.startBatch(projectId, batchId);
		
		Mockito.verify(batchService).startBatch(batchId);
	}
	
	@Test
	public void testStartBatchDoesntCallServiceIfNotAuthorized() {
		Mockito.when(userService.currentUserIsAuthorizedForBatch(projectId, batchId)).thenReturn(false);
		
		controller.startBatch(projectId, batchId);
		
		Mockito.verifyZeroInteractions(batchService);
	}
	
	@Test
	public void testPauseBatchHandleRequestView() {
		String result = controller.pauseBatch(projectId, batchId);
		
		String expected = "redirect:/project/" + projectId + "/batch/" + batchId;
		assertEquals(expected, result);
	}
	
	@Test
	public void testPauseBatchCallsService() {
		controller.pauseBatch(projectId, batchId);
		
		Mockito.verify(batchService).pauseBatch(batchId);
	}
	
	@Test
	public void testPauseBatchDoesntCallServiceIfNotAuthorized() {
		Mockito.when(userService.currentUserIsAuthorizedForBatch(projectId, batchId)).thenReturn(false);
		
		controller.pauseBatch(projectId, batchId);
		
		Mockito.verifyZeroInteractions(batchService);
	}
	
	@Test
	public void testDeleteBatchHandleRequestView() {
		String result = controller.deleteBatch(projectId, batchId);
		
		String expected = "redirect:/project/" + projectId;
		assertEquals(expected, result);
	}
	
	@Test
	public void testDeleteBatchCallsService() {
		controller.deleteBatch(projectId, batchId);
		
		Mockito.verify(batchService).removeBatch(batchId);
	}
	
	@Test
	public void testDeleteBatchDoesntCallServiceIfNotAuthorized() {
		Mockito.when(userService.currentUserIsAuthorizedForBatch(projectId, batchId)).thenReturn(false);
		
		controller.deleteBatch(projectId, batchId);
		
		Mockito.verifyZeroInteractions(batchService);
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
	public void testNewBatchDoesntAddProjectToModelIfNotAuthorized() {
		Model model = Mockito.mock(Model.class);
		Mockito.when(userService.currentUserIsAuthorizedForProject(projectId)).thenReturn(false);
		
		controller.newBatch(projectId, model);
		
		Mockito.verify(model, Mockito.times(1)).addAttribute(Mockito.any());
	}
	
	@Test
	public void testCreateBatchHandleRequestViewWithErrors() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(true);
		
		String result = controller.createBatch(batch, projectId, bindingResult, null);
		
		assertEquals("create", result);
	}
	
	@Test
	public void testCreateBatchRejectsNonCSVFiles() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(file.getContentType()).thenReturn("text");
		
		String result = controller.createBatch(batch, projectId, bindingResult, file);
		
		assertEquals("create", result);
		Mockito.verify(bindingResult).reject("error.file.format");
	}
	
	@Test
	public void testCreateBatchCallsService() {
		Project project = Mockito.mock(Project.class);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.createBatch(batch, projectId, bindingResult, null);
		
		Mockito.verify(project).addBatch(batch);
		Mockito.verify(projectService).saveProject(project);
	}

}
