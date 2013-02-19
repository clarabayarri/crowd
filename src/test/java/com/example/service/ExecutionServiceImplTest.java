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

import com.example.model.Execution;

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
		Execution execution = new Execution();
		
		service.addExecution(execution);
		
		Mockito.verify(em).persist(execution);
	}
}
