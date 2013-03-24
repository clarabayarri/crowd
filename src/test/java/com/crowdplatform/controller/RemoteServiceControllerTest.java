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

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.ExecutionInfo;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.crowdplatform.model.TaskInfo;
import com.crowdplatform.service.ExecutionService;
import com.crowdplatform.service.ProjectUserService;
import com.crowdplatform.service.TaskRetrievalStrategy;
import com.crowdplatform.service.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class RemoteServiceControllerTest {

	@InjectMocks
	private RemoteServiceController controller = new RemoteServiceController();
	
	@Mock
	private TaskService taskService;
	
	@Mock
	private TaskRetrievalStrategy taskRetrieval;
	
	@Mock
	private ExecutionService executionService;
	
	@Mock
	private ProjectUserService userService;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testProvideTaskRetrievesTask() {
		Task task = new Task();
		task.setId(3);
		Mockito.when(taskRetrieval.retrieveTaskForExecution()).thenReturn(task);
		
		TaskInfo taskInfo = controller.provideTask();
		
		assertEquals(task.getId(), taskInfo.getId());
	}
	
	@Test
	public void testSaveExecutionRetrievesAssociatedTask() {
		ExecutionInfo info = new ExecutionInfo();
		info.setTaskId(3);
		Mockito.when(taskService.getTask(3)).thenReturn(new Task());
		
		controller.saveExecution(info);
		
		Mockito.verify(taskService).getTask(3);
	}
	
	@Test
	public void testSaveExecutionCallsServiceToSave() {
		ExecutionInfo info = new ExecutionInfo();
		info.setTaskId(3);
		Mockito.when(taskService.getTask(3)).thenReturn(new Task());
		
		controller.saveExecution(info);
		
		Mockito.verify(executionService).addExecution(Mockito.any(Execution.class));
	}
	
	@Test
	public void testSaveUserCallsServiceToSave() {
		ProjectUser user = new ProjectUser();
		
		controller.saveUser(user);
		
		Mockito.verify(userService).addProjectUser(user);
	}
}
