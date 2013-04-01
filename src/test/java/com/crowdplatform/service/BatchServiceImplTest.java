package com.crowdplatform.service;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {

	@InjectMocks
	private BatchServiceImpl service = new BatchServiceImpl();
	
	@Mock
	private EntityManager em;
	
	private Batch batch = new Batch();
	private Batch mockBatch;
	
	private static final Integer batchId = 1;
	private static final Integer projectId = 2;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
		Mockito.when(em.find(Batch.class, batchId)).thenReturn(batch);
	}
	
	@Test
	public void testRemoveBatch() {
		service.removeBatch(batchId);
		
		Mockito.verify(em).remove(batch);
	}
	
	@Test
	public void testGetBatch() {
		service.getBatch(batchId);
		
		Mockito.verify(em).find(Batch.class, batchId);
	}
	
	@Test
	public void testGetBatchEagerlyLoadsRelations() {
		mockBatch = Mockito.mock(Batch.class);
	    mockBatch.setId(batchId);
		Set<Task> tasks = Sets.newHashSet();
		Mockito.when(mockBatch.getTasks()).thenReturn(tasks);
		Mockito.when(em.find(Batch.class, batchId)).thenReturn(mockBatch);
		
		service.getBatch(batchId);
		
		Mockito.verify(mockBatch).getTasks();
		Mockito.verify(mockBatch).getNumTasks();
	}
	
	@Test
	public void testListRunningBatchIds() {
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createQuery("SELECT b.id FROM Batch b, project_batch pb WHERE b.state='RUNNING' AND pb.batches_id=id AND pb.project_id='" + projectId + "'"))
			.thenReturn(query);
	
		service.listRunningBatchIds(projectId);
		
		Mockito.verify(query).getResultList();
	}
	
	@Test
	public void testStartBatch() {
		batch.setState(Batch.State.PAUSED);
		
		service.startBatch(batchId);
		
		assertEquals(Batch.State.RUNNING, batch.getState());
		Mockito.verify(em).merge(batch);
	}
	
	@Test
	public void testPauseBatch() {
		batch.setState(Batch.State.RUNNING);
		
		service.pauseBatch(batchId);
		
		assertEquals(Batch.State.PAUSED, batch.getState());
		Mockito.verify(em).merge(batch);
	}
	
	@Test
	public void testListExecutions() {
		Task task = Mockito.mock(Task.class);
		Set<Task> tasks = Sets.newHashSet(task);
		batch.setTasks(tasks);
		
		service.listExecutions(batchId);
		
		Mockito.verify(task).getExecutions();
	}
}
