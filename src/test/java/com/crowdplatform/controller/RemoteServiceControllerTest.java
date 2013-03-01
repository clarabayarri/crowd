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

import com.crowdplatform.model.Task;
import com.crowdplatform.model.TaskInfo;
import com.crowdplatform.service.TaskRetrievalStrategy;

@RunWith(MockitoJUnitRunner.class)
public class RemoteServiceControllerTest {

	@InjectMocks
	private RemoteServiceController controller = new RemoteServiceController();
	
	@Mock
	private TaskRetrievalStrategy taskService;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testProvideTaskRetrievesTask() {
		Task task = new Task();
		task.setId(3);
		Mockito.when(taskService.retrieveTaskForExecution()).thenReturn(task);
		
		TaskInfo taskInfo = controller.provideTask();
		
		assertEquals(task.getId(), taskInfo.getId());
	}
}
