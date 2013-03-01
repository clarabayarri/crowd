package com.crowdplatform.service;

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
import com.crowdplatform.model.Task;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class RandomBatchLessExecutedTaskRetrievalStrategyTest {

	@InjectMocks
	private RandomBatchLessExecutedTaskRetrievalStrategy service = new RandomBatchLessExecutedTaskRetrievalStrategy();
	
	@Mock
	private BatchService batchService;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testRetrieveTaskCallsForRunningBatchIds() {
		List<Integer> batchIds = Lists.newArrayList(1,3);
		Mockito.when(batchService.listRunningBatchIds()).thenReturn(batchIds);
		Batch batch = Mockito.mock(Batch.class);
		Task task1 = new Task();
		task1.setNumExecutions(3);
		Task task2 = new Task();
		task2.setNumExecutions(5);
		Mockito.when(batch.getTasks()).thenReturn(Sets.newHashSet(task1, task2));
		Mockito.when(batchService.getBatch(Mockito.anyInt())).thenReturn(batch);
		
		service.retrieveTaskForExecution();
		
		Mockito.verify(batchService).listRunningBatchIds();
	}
	
	@Test
	public void testRetrieveTaskGivesLessExecutedTaskFromBatch() {
		List<Integer> batchIds = Lists.newArrayList(1,3);
		Mockito.when(batchService.listRunningBatchIds()).thenReturn(batchIds);
		Batch batch = Mockito.mock(Batch.class);
		Task task1 = new Task();
		task1.setNumExecutions(3);
		Task task2 = new Task();
		task2.setNumExecutions(1);
		Mockito.when(batch.getTasks()).thenReturn(Sets.newHashSet(task1, task2));
		Mockito.when(batchService.getBatch(Mockito.anyInt())).thenReturn(batch);
		
		Task result = service.retrieveTaskForExecution();
		
		assertSame(task2, result);
	}
}
