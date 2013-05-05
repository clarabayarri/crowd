package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.ExecutionInfo;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.crowdplatform.model.TaskInfo;
import com.crowdplatform.service.BatchExecutionService;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.TaskRetrievalStrategy;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class RemoteServiceControllerTest {

	@InjectMocks
	private RemoteServiceController controller = new RemoteServiceController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private BatchExecutionService batchService;
	
	@Mock
	private TaskRetrievalStrategy taskRetrieval;
	
	private Project project = new Project();
	private Batch batch = new Batch();
	private Task task = new Task();
	private static final String projectId = "1";
	private static final Long projectUid = new Long(2);
	private static final Integer taskId = 3;
	private static final Integer batchId = 5;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    project = new Project();
	    batch = new Batch();
	    task = new Task();
	    batch.addTask(task);
	    project.addBatch(batch);
	    task.setId(taskId);
	    batch.setId(batchId);
	    project.setUid(projectUid);
	    Mockito.when(projectService.getProject(projectId)).thenReturn(project);
	    Mockito.when(batchService.getExecutions(Mockito.anyString())).thenReturn(new BatchExecutionCollection());
	}
	
	@Test
	public void testProvideTaskRetrievesTask() {
		Task task = new Task();
		task.setId(3);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 1)).thenReturn(Lists.newArrayList(task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, projectUid, 1);
		
		assertEquals(1, taskInfo.length);
		assertEquals(task.getId(), taskInfo[0].getId());
	}
	
	@Test
	public void testProvideTaskRetrievesOneTaskIfNoCountProvided() {
		Task task = new Task();
		task.setId(3);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 1)).thenReturn(Lists.newArrayList(task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, projectUid, null);
		
		assertEquals(1, taskInfo.length);
	}
	
	@Test
	public void testProvideTaskRetrievesMoreTasksIfCountProvided() {
		Task task = new Task();
		task.setId(3);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 2)).thenReturn(Lists.newArrayList(task, task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, projectUid, 2);
		
		assertEquals(2, taskInfo.length);
	}
	
	@Test
	public void testProvideTaskRetrievesNothingIfWrongCredentials() {
		TaskInfo[] taskInfo = controller.provideTask(projectId, new Long(1), 2);
		
		assertNull(taskInfo);
	}
	
	@Test
	public void testSaveExecutionCallsServiceToSave() {
		ExecutionInfo info = new ExecutionInfo();
		info.setBatchId(batchId);
		info.setTaskId(taskId);
		
		controller.saveExecution(projectId, projectUid, info);
		
		Mockito.verify(batchService).saveExecutions(Mockito.any(BatchExecutionCollection.class));
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongCredentials() {
		controller.saveExecution(projectId, new Long(3), null);
		
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongBatch() {
		ExecutionInfo info = new ExecutionInfo();
		info.setBatchId(8);
		info.setTaskId(taskId);
		
		controller.saveExecution(projectId, projectUid, info);
		
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongTask() {
		ExecutionInfo info = new ExecutionInfo();
		info.setBatchId(batchId);
		info.setTaskId(8);
		
		controller.saveExecution(projectId, projectUid, info);
		
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testSaveUserCallsServiceToSave() {
		ProjectUser user = new ProjectUser();
		
		controller.saveUser(projectId, projectUid, user);
		
		Mockito.verify(projectService).saveProject(Mockito.any(Project.class));
	}
	
	@Test
	public void testSaveUserReturnsZeroIfGoneWrong() {
		ProjectUser user = new ProjectUser();
		
		int result = controller.saveUser(projectId, new Long(3), user);
		
		assertEquals(0, result);
	}
	
	@Test
	public void testSaveUserReturnsUserIdIfCorrect() {
		ProjectUser user = new ProjectUser();
		
		int result = controller.saveUser(projectId, projectUid, user);
		
		assertEquals(1, result);
	}
	
	@Test
	public void testSaveUserDoesNothingIfWrongCredentials() {
		controller.saveUser(projectId, new Long(3), null);
		
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
}
