package com.crowdplatform.model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Task;
import com.google.common.collect.Sets;



public class BatchTest {

	private Batch batch = new Batch();
	
	@Mock
	private Task zeroExecutionsTask = new Task();
	
	@Mock
	private Task oneExecutionTask = new Task();
	
	@Mock
	private Task twoExecutionsTask = new Task();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		batch.setExecutionsPerTask(2);
		Mockito.when(zeroExecutionsTask.getNumExecutions()).thenReturn(0);
		Mockito.when(oneExecutionTask.getNumExecutions()).thenReturn(1);
		Mockito.when(twoExecutionsTask.getNumExecutions()).thenReturn(2);
	}
	
	@Test
	public void testPercentageCompleteWhenEmpty() {
		Set<Task> tasks = Sets.newHashSet();
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(0, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteWhenNoExecutions() {
		Set<Task> tasks = Sets.newHashSet();
		tasks.add(zeroExecutionsTask);
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(0, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteWhenPartialExecutions() {
		Set<Task> tasks = Sets.newHashSet();
		tasks.add(oneExecutionTask);
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(50, result, 0.01);
	}
	
	@Test
	public void testPercentageCompleteWhenPartialExecutions2() {
		Set<Task> tasks = Sets.newHashSet();
		tasks.add(oneExecutionTask);
		tasks.add(twoExecutionsTask);
		batch.setTasks(tasks);
		
		double result = batch.getPercentageComplete();
		
		assertEquals(75, result, 0.01);
	}
}
