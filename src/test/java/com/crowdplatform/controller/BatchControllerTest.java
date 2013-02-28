package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
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
import com.crowdplatform.service.BatchService;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {

	@InjectMocks
	private BatchController controller = new BatchController();
	
	@Mock
	private BatchService service;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testListBatchesHandleRequestView() {
		String result = controller.listBatches(new HashMap<String, Object>());
		assertEquals("batches", result);
	}
	
	@Test
	public void testListBatchesRetrievesBatchesToMap() {
		List<Batch> batches = Lists.newArrayList(new Batch(), new Batch());
		Mockito.when(service.listBatches()).thenReturn(batches);
		
		HashMap<String, Object> model = new HashMap<String, Object>();
		controller.listBatches(model);
		
		assertSame(batches, model.get("batchList"));
		Mockito.verify(service).listBatches();
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		String result = controller.getBatch(1, new HashMap<String, Object>());
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToMap() {
		Batch batch = new Batch();
		Mockito.when(service.getBatch(1)).thenReturn(batch);
		
		HashMap<String, Object> model = new HashMap<String, Object>();
		controller.getBatch(1, model);
		
		assertSame(batch, model.get("batch"));
		Mockito.verify(service).getBatch(1);
	}

}
