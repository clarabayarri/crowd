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
	
	private Project project = new Project();
	private static final String projectId = "1";
	private static final Long projectUid = new Long(2);
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    project.setUid(projectUid);
	    Mockito.when(projectService.getProject(projectId)).thenReturn(project);
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
	public void testSaveExecutionRetrievesAssociatedTask() {
		ExecutionInfo info = new ExecutionInfo();
		info.setTaskId(3);
		Mockito.when(taskService.getTask(projectId,3)).thenReturn(new Task());
		
		controller.saveExecution(projectId, projectUid, info);
		
		Mockito.verify(taskService).getTask(projectId,3);
	}
	
	@Test
	public void testSaveExecutionCallsServiceToSave() {
		ExecutionInfo info = new ExecutionInfo();
		info.setTaskId(3);
		Task task = new Task();
		Mockito.when(taskService.getTask(projectId,3)).thenReturn(task);
		
		controller.saveExecution(projectId, projectUid, info);
		
		Mockito.verify(taskService).saveTask(task);
	}
	
	@Test
	public void testSaveExecutionDoesNothingIfWrongCredentials() {
		controller.saveExecution(projectId, new Long(3), null);
		
		Mockito.verifyZeroInteractions(taskService);
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
		user.setId(30);
		
		int result = controller.saveUser(projectId, projectUid, user);
		
		assertEquals(30, result);
	}
	
	@Test
	public void testSaveUserDoesNothingIfWrongCredentials() {
		controller.saveUser(projectId, new Long(3), null);
		
		Mockito.verifyZeroInteractions(userService);
	}
}
