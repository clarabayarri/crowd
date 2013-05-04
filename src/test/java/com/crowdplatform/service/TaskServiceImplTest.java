package com.crowdplatform.service;

import static org.junit.Assert.assertNull;

import java.math.BigInteger;

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

import com.crowdplatform.model.Task;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

	@InjectMocks
	private TaskServiceImpl service = new TaskServiceImpl();
	
	@Mock
	private EntityManager em;
	
	private Task task = new Task();
	private static final String projectId = "1";
	private static final Integer taskId = 1;
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	    
	    Mockito.when(em.find(Task.class, taskId)).thenReturn(task);
	}
	
	@Test
	public void testSaveTask() {
		service.saveTask(task);
		
		Mockito.verify(em).merge(task);
	}
	
	@Test
	public void testRemoveTask() {
		service.removeTask(taskId);
		
		Mockito.verify(em).remove(task);
	}
	
	@Test
	public void testGetTask() {
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createNativeQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(BigInteger.ONE);
		
		service.getTask(projectId, taskId);
		
		Mockito.verify(em).find(Task.class, taskId);
	}
	
	@Test
	public void testGetTaskReturnsNullIfTaskDoesntMatchProject() {
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createNativeQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(BigInteger.ZERO);
		
		Task result = service.getTask(projectId, taskId);
		
		assertNull(result);
	}
}
