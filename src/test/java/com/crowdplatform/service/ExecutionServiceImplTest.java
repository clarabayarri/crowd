package com.crowdplatform.service;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.Execution;
import com.crowdplatform.model.Task;
import com.crowdplatform.service.ExecutionServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ExecutionServiceImplTest {

	@InjectMocks
	private ExecutionServiceImpl service = new ExecutionServiceImpl();
	
	@Mock
	private EntityManager em;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddExecution() {
		Execution execution = new Execution("", new Task());
		
		service.addExecution(execution);
		
		Mockito.verify(em).persist(execution);
	}
}
