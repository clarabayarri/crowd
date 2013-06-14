package com.crowdplatform.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;



public class BatchTest {

	private Batch batch;
	
	private Task zeroExecutionsTask;
	private Task oneExecutionTask;
	private Task twoExecutionsTask;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		batch = new Batch();
		batch.setExecutionsPerTask(2);
		zeroExecutionsTask = new Task();
		zeroExecutionsTask.setNumExecutions(0);
		oneExecutionTask = new Task();
		oneExecutionTask.setNumExecutions(1);
		twoExecutionsTask = new Task();
		twoExecutionsTask.setNumExecutions(2);
	}
	
	@Test
	public void testInitialValues() {
		Batch batch = new Batch();
		
		assertNotNull(batch.getCreationDate());
		assertNotNull(batch.getTasks());
		assertNotNull(batch.getExecutionsPerTask());
		assertEquals(Batch.State.PAUSED, batch.getState());
		assertEquals(0, batch.getNumCompletedTasks().intValue());
	}
	
	@Test
	public void testAddTaskAssignsFirstId() {
		Task task = new Task();
		
		batch.addTask(task);
		
		assertEquals(1, task.getId().intValue());
	}
	
	@Test
	public void testAddTaskAssignsOtherTaskIds() {
		List<Task> tasks = Lists.newArrayList(oneExecutionTask, zeroExecutionsTask);
		oneExecutionTask.setTaskId(10);
		zeroExecutionsTask.setTaskId(33);
		batch.setTasks(tasks);
		Task task = new Task();
		
		batch.addTask(task);
		
		assertEquals(33 + 1, task.getId().intValue());
	}
	
	@Test
	public void testPercentageCompleteWhenEmpty() {
		List<Task> tasks = Lists.newArrayList();
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(0, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteWhenNoExecutions() {
		List<Task> tasks = Lists.newArrayList(zeroExecutionsTask);
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(0, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteWhenPartialExecutions() {
		List<Task> tasks = Lists.newArrayList(oneExecutionTask);
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(50, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteWhenPartialExecutions2() {
		List<Task> tasks = Lists.newArrayList(oneExecutionTask, twoExecutionsTask);
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(75, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteUpdatesStateWhenComplete() {
		List<Task> tasks = Lists.newArrayList(twoExecutionsTask);
		batch.setTasks(tasks);
		batch.setExecutionsPerTask(2);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(100, result, 0.01);
		assertEquals(Batch.State.COMPLETE, batch.getState());
	}
}
