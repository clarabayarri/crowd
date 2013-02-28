package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

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
		Model model = Mockito.mock(Model.class);
		
		String result = controller.listBatches(model);
		
		assertEquals("batches", result);
	}
	
	@Test
	public void testListBatchesRetrievesBatchesToModel() {
		List<Batch> batches = Lists.newArrayList(new Batch(), new Batch());
		Mockito.when(service.listBatches()).thenReturn(batches);
		Model model = Mockito.mock(Model.class);
		
		controller.listBatches(model);
		
		Mockito.verify(service).listBatches();
		Mockito.verify(model).addAttribute(batches);
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getBatch(1, model);
		
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToModel() {
		Batch batch = new Batch();
		Mockito.when(service.getBatch(1)).thenReturn(batch);
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(1, model);
		
		Mockito.verify(model).addAttribute(batch);
		Mockito.verify(service).getBatch(1);
	}
	
	@Test
	public void testStartBatchCallsService() {
		controller.startBatch(1);
		
		Mockito.verify(service).startBatch(1);
	}
	
	@Test
	public void testPauseBatchCallsService() {
		controller.pauseBatch(1);
		
		Mockito.verify(service).pauseBatch(1);
	}
	
	@Test
	public void testNewBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.newBatch(model);
		
		assertEquals("create", result);
	}
	
	@Test
	public void testNewBatchAddsEmptyBatchToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.newBatch(model);
		
		Mockito.verify(model).addAttribute(Mockito.any(Batch.class));
	}

}
