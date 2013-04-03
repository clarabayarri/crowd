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

import com.crowdplatform.model.ExecutionInfo;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.ProjectUser;
import com.crowdplatform.model.Task;
import com.crowdplatform.model.TaskInfo;
import com.crowdplatform.service.ProjectService;
import com.crowdplatform.service.ProjectUserService;
import com.crowdplatform.service.TaskRetrievalStrategy;
import com.crowdplatform.service.TaskService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class RemoteServiceControllerTest {

	@InjectMocks
	private RemoteServiceController controller = new RemoteServiceController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private TaskService taskService;
	
	@Mock
	private TaskRetrievalStrategy taskRetrieval;
	
	@Mock
	private ProjectUserService userService;
	
	private static final Integer projectId = 1;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testProvideTaskRetrievesTask() {
		Task task = new Task();
		task.setId(3);
		Mockito.when(taskRetrieval.retrieveTasksForExecution(projectId, 1)).thenReturn(Lists.newArrayList(task));
		
		TaskInfo[] taskInfo = controller.provideTask(projectId, 1);
		
		assertEquals(1, taskInfo.length);
		assertEquals(task.getId(), taskInfo[0].getId());
	}
	
	@Test
	public void testSaveExecutionRetrievesAssociatedTask() {
		ExecutionInfo info = new ExecutionInfo();
		info.setTaskId(3);
		Mockito.when(taskService.getTask(1,3)).thenReturn(new Task());
		
		controller.saveExecution(projectId, info);
		
		Mockito.verify(taskService).getTask(1,3);
	}
	
	@Test
	public void testSaveExecutionCallsServiceToSave() {
		ExecutionInfo info = new ExecutionInfo();
		info.setTaskId(3);
		Task task = new Task();
		Mockito.when(taskService.getTask(1,3)).thenReturn(task);
		
		controller.saveExecution(projectId, info);
		
		Mockito.verify(taskService).saveTask(task);
	}
	
	@Test
	public void testSaveUserCallsServiceToSave() {
		ProjectUser user = new ProjectUser();
		Mockito.when(projectService.getProject(projectId)).thenReturn(new Project());
		
		controller.saveUser(projectId, user);
		
		Mockito.verify(projectService).saveProject(Mockito.any(Project.class));
	}
}
