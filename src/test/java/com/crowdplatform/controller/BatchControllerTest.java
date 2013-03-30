package com.crowdplatform.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Project;
import com.crowdplatform.service.BatchService;
import com.crowdplatform.service.ProjectService;

@RunWith(MockitoJUnitRunner.class)
public class BatchControllerTest {

	@InjectMocks
	private BatchController controller = new BatchController();
	
	@Mock
	private ProjectService projectService;
	
	@Mock
	private BatchService service;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		
		String result = controller.getBatch(1, 1, model, null);
		
		assertEquals("batch", result);
	}
	
	@Test
	public void testGetBatchRetrievesBatchToModel() {
		Batch batch = new Batch();
		Mockito.when(service.getBatch(1)).thenReturn(batch);
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(1, 1, model, null);
		
		Mockito.verify(model).addAttribute(batch);
		Mockito.verify(service).getBatch(1);
	}
	
	@Test
	public void testGetBatchAddsCreatedParameterIfProvided() {
		Model model = Mockito.mock(Model.class);
		
		controller.getBatch(1, 1, model, true);
		
		Mockito.verify(model).addAttribute("created", true);
	}
	
	@Test
	public void testStartBatchCallsService() {
		controller.startBatch(1, 1);
		
		Mockito.verify(service).startBatch(1);
	}
	
	@Test
	public void testPauseBatchCallsService() {
		controller.pauseBatch(1, 1);
		
		Mockito.verify(service).pauseBatch(1);
	}
	
	@Test
	public void testNewBatchHandleRequestView() {
		Model model = Mockito.mock(Model.class);
		Mockito.when(projectService.getProject(1)).thenReturn(new Project());
		
		String result = controller.newBatch(1, model);
		
		assertEquals("create", result);
	}
	
	@Test
	public void testNewBatchAddsEmptyBatchToModel() {
		Model model = Mockito.mock(Model.class);
		
		controller.newBatch(1, model);
		
		Mockito.verify(model).addAttribute(Mockito.any(Batch.class));
	}
	
	@Test
	public void testCreateBatchCallsService() {
		Batch batch = new Batch();
		Mockito.when(projectService.getProject(1)).thenReturn(new Project());
		BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(bindingResult.hasErrors()).thenReturn(false);
		
		controller.createBatch(batch, 1, bindingResult, null);
		
		Mockito.verify(service).createBatch(batch, 1);
	}

}
