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

import com.crowdplatform.aux.ExecutionInfo;
import com.crowdplatform.aux.ProjectUserInfo;
import com.crowdplatform.aux.TaskInfo;
import com.crowdplatform.aux.TaskRequest;
import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.Task;
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
	    batch.setState(Batch.State.RUNNING);
	    project.addBatch(batch);
	    task.setTaskId(taskId);
	    task.setBatchId(batchId);
	    batch.setId(batchId);
	    project.setUid(projectUid);
	    Mockito.when(projectService.getProject(projectId)).thenReturn(project);
	    Mockito.when(batchService.getExecutions(Mockito.anyString())).thenReturn(new BatchExecutionCollection());
	}
	
	@Test
	public void testProvideTaskRetrievesTask() {
		Task task = new Task();
		task.setTaskId(3);
		TaskRequest request = new TaskRequest();
		request.setCount(1);
		request.setProjectUid(projectUid);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 1)).thenReturn(Lists.newArrayList(task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, request);
		
		assertEquals(1, taskInfo.length);
		assertEquals(task.getId(), taskInfo[0].getTaskId());
	}
	
	@Test
	public void testProvideTaskRetrievesOneTaskIfNoCountProvided() {
		Task task = new Task();
		task.setTaskId(3);
		TaskRequest request = new TaskRequest();
		request.setProjectUid(projectUid);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 1)).thenReturn(Lists.newArrayList(task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, request);
		
		assertEquals(1, taskInfo.length);
	}
	
	@Test
	public void testProvideTaskRetrievesMoreTasksIfCountProvided() {
		Task task = new Task();
		task.setTaskId(3);
		TaskRequest request = new TaskRequest();
		request.setCount(2);
		request.setProjectUid(projectUid);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 2)).thenReturn(Lists.newArrayList(task, task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, request);
		
		assertEquals(2, taskInfo.length);
	}
	
	@Test
	public void testProvideTaskRetrievesNothingIfWrongCredentials() {
		TaskRequest request = new TaskRequest();
		request.setCount(2);
		request.setProjectUid(new Long(1));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, request);
		
		assertNull(taskInfo);
	}
	
	@Test
	public void testSaveExecutionCallsServiceToSave() {
		ExecutionInfo info = new ExecutionInfo();
		info.setBatchId(batchId);
		info.setTaskId(taskId);
		info.setProjectUid(projectUid);
		
		controller.saveExecution(projectId, info);
		
		Mockito.verify(batchService).saveExecutions(Mockito.any(BatchExecutionCollection.class));
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongCredentials() {
		ExecutionInfo info = new ExecutionInfo();
		info.setProjectUid(new Long(3));
		
		controller.saveExecution(projectId, info);
		
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongBatch() {
		ExecutionInfo info = new ExecutionInfo();
		info.setBatchId(8);
		info.setTaskId(taskId);
		info.setProjectUid(projectUid);
		
		controller.saveExecution(projectId, info);
		
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongTask() {
		ExecutionInfo info = new ExecutionInfo();
		info.setBatchId(batchId);
		info.setTaskId(8);
		info.setProjectUid(projectUid);
		
		controller.saveExecution(projectId, info);
		
		Mockito.verifyZeroInteractions(batchService);
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
	
	@Test
	public void testSaveUserCallsServiceToSave() {
		ProjectUserInfo user = new ProjectUserInfo();
		user.setProjectUid(projectUid);
		
		controller.saveUser(projectId, user);
		
		Mockito.verify(projectService).saveProject(Mockito.any(Project.class));
	}
	
	@Test
	public void testSaveUserReturnsZeroIfGoneWrong() {
		ProjectUserInfo user = new ProjectUserInfo();
		user.setProjectUid(new Long(3));
		
		int result = controller.saveUser(projectId, user);
		
		assertEquals(0, result);
	}
	
	@Test
	public void testSaveUserReturnsUserIdIfCorrect() {
		ProjectUserInfo user = new ProjectUserInfo();
		user.setProjectUid(projectUid);
		
		int result = controller.saveUser(projectId, user);
		
		assertEquals(1, result);
	}
	
	@Test
	public void testSaveUserDoesNothingIfWrongCredentials() {
		ProjectUserInfo user = new ProjectUserInfo();
		user.setProjectUid(new Long(3));
		
		controller.saveUser(projectId, user);
		
		Mockito.verify(projectService, Mockito.never()).saveProject(project);
	}
}
