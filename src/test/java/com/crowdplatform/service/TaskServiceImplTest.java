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

import com.crowdplatform.model.Task;
import com.crowdplatform.service.TaskServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

	@InjectMocks
	private TaskServiceImpl service = new TaskServiceImpl();
	
	@Mock
	private EntityManager em;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddTask() {
		Task task = new Task();
		
		service.addTask(task);
		
		Mockito.verify(em).persist(task);
	}
}
