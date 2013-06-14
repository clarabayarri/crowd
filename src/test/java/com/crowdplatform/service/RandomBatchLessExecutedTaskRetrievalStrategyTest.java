package com.crowdplatform.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Project;
import com.crowdplatform.model.Task;

@RunWith(MockitoJUnitRunner.class)
public class RandomBatchLessExecutedTaskRetrievalStrategyTest {

	@InjectMocks
	private RandomBatchLessExecutedTaskRetrievalStrategy service = new RandomBatchLessExecutedTaskRetrievalStrategy();
	
	@Mock
	private ProjectService projectService;
	
	private static final String projectId = "1";
	private Project project;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    project = new Project();
	    Mockito.when(projectService.getProject(Mockito.anyString())).thenReturn(project);
	}
	
	@Test
	public void testGetRandomBatchRetrievesBatchFromProject() {
		Batch batch = new Batch();
		batch.setState(Batch.State.RUNNING);
		project.addBatch(batch);
		
		Batch result = service.getRandomBatch(projectId);
		
		assertEquals(batch, result);
	}
	
	@Test
	public void testGetRandomBatchRetrievesCompletedBatchIfNoRunning() {
		Batch batch = new Batch();
		batch.setState(Batch.State.COMPLETE);
		project.addBatch(batch);
		
		Batch result = service.getRandomBatch(projectId);
		
		assertEquals(batch, result);
	}
	
	@Test
	public void testGetRandomBatchFakesBatchIfNoneAvailable() {
		Batch result = service.getRandomBatch(projectId);
		
		assertNotNull(result);
	}
	
	@Test
	public void testGetLessExecutedTasksForBatch() {
		Batch batch = new Batch();
		batch.setState(Batch.State.RUNNING);
		project.addBatch(batch);
		Task task1 = new Task();
		task1.setNumExecutions(3);
		batch.addTask(task1);
		Task task2 = new Task();
		task2.setNumExecutions(1);
		batch.addTask(task2);
		
		List<Task> result = service.getLessExecutedTasksForBatch(batch, 1);
		
		assertEquals(1, result.size());
		assertSame(task2, result.get(0));
	}
	
	@Test
	public void testGetLessExecutedTasksForBatchWithTwoTasks() {
		Batch batch = new Batch();
		batch.setState(Batch.State.RUNNING);
		project.addBatch(batch);
		Task task1 = new Task();
		task1.setNumExecutions(3);
		batch.addTask(task1);
		Task task2 = new Task();
		task2.setNumExecutions(1);
		batch.addTask(task2);
		
		List<Task> result = service.getLessExecutedTasksForBatch(batch, 2);
		
		assertEquals(2, result.size());
		assertSame(task2, result.get(0));
		assertSame(task1, result.get(1));
	}
	
	@Test
	public void testGetLessExecutedTasksForBatchGivesMinNumberBetweenAvailableAndRequested() {
		Batch batch = new Batch();
		batch.setState(Batch.State.RUNNING);
		project.addBatch(batch);
		Task task1 = new Task();
		task1.setNumExecutions(3);
		batch.addTask(task1);
		Task task2 = new Task();
		task2.setNumExecutions(1);
		batch.addTask(task2);
		
		List<Task> result = service.getLessExecutedTasksForBatch(batch, 7);
		
		assertEquals(2, result.size());
		assertSame(task2, result.get(0));
		assertSame(task1, result.get(1));
	}
	
	@Test
	public void testRetrieveTasksForExecutionJoinsLogic() {
		Batch batch = new Batch();
		batch.setState(Batch.State.RUNNING);
		project.addBatch(batch);
		Task task1 = new Task();
		task1.setNumExecutions(3);
		batch.addTask(task1);
		Task task2 = new Task();
		task2.setNumExecutions(1);
		batch.addTask(task2);
		
		List<Task> result = service.retrieveTasksForExecution("project", 7);
		
		assertNotNull(result);
	}
}
