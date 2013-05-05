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
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.google.common.collect.Lists;

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
	
	private Batch batch = new Batch();
	private Project project = new Project();
	private PlatformUser user = new PlatformUser();
	private static final String projectId = "1";
	private static String username = "username";
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    user = new PlatformUser();
	    project = new Project();
	    project.setId(projectId);
	    project.addBatch(batch);
	    project.setOwnerId(username);
	    user.setUsername(username);
	    List<Project> projects = Lists.newArrayList(project);
	    Mockito.when(projectService.getProjectsForUser(username)).thenReturn(projects);
	    Mockito.when(userService.getCurrentUser()).thenReturn(user);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getBatch(projectId, batch.getId(), model, null, null);
		
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToModelIfAuthorized() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batch.getId(), model, null, null);
		
		Mockito.verify(model).addAttribute(batch);
		Mockito.verify(projectService).getProject(projectId);
	}
	
	@Test
	public void testGetBatchDoesntRetrievesBatchToModelIfNotAuthorized() {
		Model model = Mockito.mock(Model.class);
		project.setOwnerId("other username");
		
		controller.getBatch(projectId, batch.getId(), model, null, null);
		
		Mockito.verifyZeroInteractions(model);
	}
	
	@Test
	public void testGetBatchAddsCreatedParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batch.getId(), model, true, null);
		
		Mockito.verify(model).addAttribute("created", true);
	}
	
	@Test
	public void testGetBatchAddsExportErrorParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(projectId, batch.getId(), model, null, true);
		
		Mockito.verify(model).addAttribute("export-error", true);
	}
	
	@Test
	public void testStartBatchHandleRequestView() {
		String result = controller.startBatch(projectId, batch.getId());
		
		String expected = "redirect:/project/" + projectId + "/batch/" + batch.getId();
		assertEquals(expected, result);
	}
	
	@Test
	public void testStartBatchChangesStateAndSaves() {
		batch.setState(Batch.State.PAUSED);
		
		controller.startBatch(projectId, batch.getId());
		
		assertEquals(Batch.State.RUNNING, batch.getState());
		Mockito.verify(projectService).saveProject(project);
	}
	
	@Test
	public void testStartBatchDoesntChangeStateIfNotAuthorized() {
		batch.setState(Batch.State.PAUSED);
		project.setOwnerId("other username");
		
		controller.startBatch(projectId, batch.getId());
		
		assertEquals(Batch.State.PAUSED, batch.getState());
	}
	
	@Test
	public void testPauseBatchHandleRequestView() {
		String result = controller.pauseBatch(projectId, batch.getId());
		
		String expected = "redirect:/project/" + projectId + "/batch/" + batch.getId();
		assertEquals(expected, result);
	}
	
	@Test
	public void testPauseBatchCallsService() {
		batch.setState(Batch.State.RUNNING);
		
		controller.pauseBatch(projectId, batch.getId());
		
		assertEquals(Batch.State.PAUSED, batch.getState());
		Mockito.verify(projectService).saveProject(project);
	}
	
	@Test
	public void testPauseBatchDoesntCallServiceIfNotAuthorized() {
		batch.setState(Batch.State.RUNNING);
		project.setOwnerId("other username");
		
		controller.pauseBatch(projectId, batch.getId());
		
		assertEquals(Batch.State.RUNNING, batch.getState());
	}
	
	@Test
	public void testDeleteBatchHandleRequestView() {
		String result = controller.deleteBatch(projectId, batch.getId());
		
		String expected = "redirect:/project/" + projectId;
		assertEquals(expected, result);
	}
	
	@Test
	public void testDeleteBatchSavesProject() {
		controller.deleteBatch(projectId, batch.getId());
		
		Mockito.verify(projectService).saveProject(project);
		assertEquals(0, project.getBatches().size());
	}
	
	@Test
	public void testDeleteBatchDoesntCallServiceIfNotAuthorized() {
		project.setOwnerId("other username");
		
		controller.deleteBatch(projectId, batch.getId());
		
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
		assertEquals(1, project.getBatches().size());
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
		project.setOwnerId("other username");
		
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
		Mockito.when(project.getOwnerId()).thenReturn(username);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.createBatch(batch, projectId, bindingResult, null);
		
		Mockito.verify(project).addBatch(batch);
		Mockito.verify(projectService).saveProject(project);
	}

}
