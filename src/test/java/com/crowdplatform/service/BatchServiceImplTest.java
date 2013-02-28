package com.crowdplatform.service;

import static org.junit.Assert.assertEquals;

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
import com.crowdplatform.service.BatchServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {

	@InjectMocks
	private BatchServiceImpl service = new BatchServiceImpl();
	
	@Mock
	private EntityManager em;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddBatch() {
		Batch batch = new Batch();
		
		service.addBatch(batch);
		
		Mockito.verify(em).persist(batch);
	}
	
	@Test
	public void testRemoveBatch() {
		Batch newBatch = new Batch();
		Mockito.when(em.find(Batch.class, 1)).thenReturn(newBatch);
		
		service.removeBatch(1);
		
		Mockito.verify(em).remove(newBatch);
	}
	
	@Test
	public void testSaveBatch() {
		Batch batch = new Batch();
		
		service.saveBatch(batch);
		
		Mockito.verify(em).merge(batch);
	}
	
	@Test
	public void testStartBatch() {
		Batch batch = new Batch();
		batch.setState(Batch.State.PAUSED);
		Mockito.when(em.find(Batch.class, 1)).thenReturn(batch);
		
		service.startBatch(1);
		
		assertEquals(Batch.State.RUNNING, batch.getState());
	}
	
	@Test
	public void testPauseBatch() {
		Batch batch = new Batch();
		batch.setState(Batch.State.RUNNING);
		Mockito.when(em.find(Batch.class, 1)).thenReturn(batch);
		
		service.pauseBatch(1);
		
		assertEquals(Batch.State.PAUSED, batch.getState());
	}
}
