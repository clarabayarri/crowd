package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.PlatformUserService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.util.FileReader;
import com.crowdplatform.util.FileWriter;
import com.crowdplatform.util.GoogleFusiontablesAdapter;
import com.crowdplatform.util.TaskCreator;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {

	@InjectMocks
	private BatchController controller = new BatchController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private BatchExecutionService batchService;
	
	@Mock
	private PlatformUserService userService;
	
	@Mock
	private GoogleFusiontablesAdapter dataExporter;
	
	@Mock
	private TaskCreator taskCreator;
	
	@Mock
	private FileWriter fileWriter;
	
	@Mock
	private FileReader fileReader;
	
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
	    batch.setName("batch name");
	    user.setUsername(username);
	    List<Project> projects = Lists.newArrayList(project);
	    Mockito.when(projectService.getProjectsForUser(user)).thenReturn(projects);
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
		
		Mockito.verify(model).addAttribute(project);
		Mockito.verify(model).addAttribute(batch);
	}
	
	@Test
	public void testGetBatchDoesntRetrievesBatchToModelIfNotAuthorized() {
		Model model = Mockito.mock(Model.class);
		project.setOwnerId("other username");
		
		controller.getBatch(projectId, batch.getId(), model, null, null);
		
		Mockito.verify(model, Mockito.never()).addAttribute(project);
		Mockito.verify(model, Mockito.never()).addAttribute(batch);
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
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testPauseBatchHandleRequestView() {
		String result = controller.pauseBatch(projectId, batch.getId());
		
		String expected = "redirect:/project/" + projectId + "/batch/" + batch.getId();
		assertEquals(expected, result);
	}
	
	@Test
	public void testPauseBatchChangesStateAndSaves() {
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
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
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
	public void testDeleteBatchDoesntSaveIfNotAuthorized() {
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
		
		Mockito.verify(model).addAttribute(project);
		Mockito.verify(model, Mockito.times(2)).addAttribute(Mockito.any());
	}
	
	@Test
	public void testNewBatchDoesntAddProjectToModelIfNotAuthorized() {
		Model model = Mockito.mock(Model.class);
		project.setOwnerId("other username");
		
		controller.newBatch(projectId, model);
		
		Mockito.verify(model, Mockito.never()).addAttribute(project);
		Mockito.verify(model).addAttribute(Mockito.any(Batch.class));
	}
	
	@Test
	public void testCreateBatchHandleRequestViewWithErrors() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(true);
		
		String result = controller.createBatch(batch, projectId, bindingResult, null);
		
		assertEquals("create", result);
	}
	
	@Test
	public void testCreateBatchDoesntModifyProjectIfNotAuthorized() {
		project.setOwnerId("other user");
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.createBatch(batch, projectId, bindingResult, null);
		
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
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
	public void testCreateBatchAcceptsCSVFiles() {
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(file.getContentType()).thenReturn("text/csv");
		
		controller.createBatch(batch, projectId, bindingResult, file);
		
		Mockito.verify(bindingResult, Mockito.never()).reject("error.file.format");
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
		Mockito.verify(projectService, Mockito.atLeastOnce()).saveProject(project);
	}
	
	@Test
	public void testCreateBatchRejectsAndReturnsIfIOException() throws IOException {
		Project project = new Project();
		project.setOwnerId(username);
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		MultipartFile file = Mockito.mock(MultipartFile.class);
		Mockito.when(file.getContentType()).thenReturn("text/csv");
		Mockito.when(fileReader.readCSVFile(file)).thenThrow(new IOException());
		
		String result = controller.createBatch(batch, projectId, bindingResult, file);
		
		Mockito.verify(bindingResult).reject("error.file.contents");
		assertEquals("create", result);
	}
	
	@Test
	public void testDownloadBatchDoesNothingIfNotAuthorized() {
		project.setOwnerId("other user");
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		controller.downloadBatch(projectId, 1, response);
		
		Mockito.verifyZeroInteractions(response);
	}
	
	@Test
	public void testDownloadBatchWritesWriterOutput() throws IOException {
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		PrintWriter writer = Mockito.mock(PrintWriter.class);
		Mockito.when(response.getWriter()).thenReturn(writer);
		String output = "This is an output.";
		Mockito.when(fileWriter.writeTasksExecutions(Mockito.any(Project.class), 
				Mockito.any(Batch.class), Mockito.any(BatchExecutionCollection.class), 
				Mockito.anyBoolean())).thenReturn(output);
		
		controller.downloadBatch(projectId, 1, response);
		
		Mockito.verify(writer).write(output);
	}
	
	@Test
	public void testDownloadBatchThrowsRuntimeExceptionIfIOError() throws IOException {
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		Mockito.when(fileWriter.writeTasksExecutions(Mockito.any(Project.class), 
				Mockito.any(Batch.class), Mockito.any(BatchExecutionCollection.class), 
				Mockito.anyBoolean())).thenThrow(new IOException());
		
		try {
			controller.downloadBatch(projectId, 1, response);
			fail("Runtime Exception not raised.");
		} catch (RuntimeException e) {
			assertEquals("IOError writing file to output stream", e.getMessage());
		}
	}
	
	@Test
	public void testViewBatchDataHandleRequestViewIfNotAuthorized() {
		project.setOwnerId("other user");
		
		String result = controller.viewBatchData(projectId, 1, null);
		
		assertEquals("/project/" + projectId + "/batch/1?export-error=true", result);
	}
	
	@Test
	public void testViewBatchDataHandleRequestView() {
		String url = "url";
		Mockito.when(dataExporter.getDataURL(
				Mockito.eq(project), 
				Mockito.eq(batch), 
				Mockito.any(BatchExecutionCollection.class), Mockito.any(TokenResponse.class)))
				.thenReturn(url);
		
		String result = controller.viewBatchData(projectId, 1, "");
		
		assertEquals(url, result);
		Mockito.verify(dataExporter).getDataURL(
				Mockito.eq(project), 
				Mockito.eq(batch), 
				Mockito.any(BatchExecutionCollection.class), Mockito.any(TokenResponse.class));
	}

}
