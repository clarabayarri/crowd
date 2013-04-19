package com.crowdplatform.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

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
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {

	@InjectMocks
	private BatchServiceImpl service = new BatchServiceImpl();
	
	@Mock
	private EntityManager em;
	
	@Mock
	private ProjectService projectService;
	
	private Batch batch = new Batch();
	private Batch mockBatch;
	
	private static final Integer batchId = 1;
	private static final Long projectId = new Long(2);
	
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
		Batch batch = new Batch();
		batch.setId(1);
		batch.setState(Batch.State.RUNNING);
		Batch batch2 = new Batch();
		batch2.setId(2);
		Project project = new Project();
		project.setBatches(Sets.newHashSet(batch, batch2));
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
	
		List<Integer> result = service.listRunningBatchIds(projectId);
		
		assertEquals(1, result.size());
		assertSame(1, result.get(0));
	}
	
	@Test
	public void testListCompletedBatchIds() {
		Batch batch = new Batch();
		batch.setId(1);
		batch.setState(Batch.State.COMPLETE);
		Batch batch2 = new Batch();
		batch2.setId(2);
		Project project = new Project();
		project.setBatches(Sets.newHashSet(batch, batch2));
		Mockito.when(projectService.getProject(projectId)).thenReturn(project);
	
		List<Integer> result = service.listCompletedBatchIds(projectId);
		
		assertEquals(1, result.size());
		assertSame(1, result.get(0));
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
		
		service.listTasksWithExecutions(batchId);
		
		Mockito.verify(task).getExecutions();
	}
}
