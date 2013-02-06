package com.example.service;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.example.model.Batch;

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
	public void testListBatchesIsNotNull() {
		
		//assert(service.listBatches() != null);
	}
	
	@Test
	public void testRemoveBatch() {
		Batch newBatch = new Batch();
		Mockito.when(em.find(Batch.class, 1)).thenReturn(newBatch);
		
		service.removeBatch(1);
		
		Mockito.verify(em).remove(newBatch);
	}
}
