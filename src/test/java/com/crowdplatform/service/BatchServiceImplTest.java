package com.crowdplatform.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;

import com.crowdplatform.model.BatchExecutionCollection;

@RunWith(MockitoJUnitRunner.class)
public class BatchServiceImplTest {

	@InjectMocks
	private BatchExecutionServiceMongoImpl service = new BatchExecutionServiceMongoImpl();
	
	@Mock
	private MongoOperations mongoOperation;
	
	private static final String collectionId = "a1";
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetExecutions() {
		service.getExecutions(collectionId);
		
		Mockito.verify(mongoOperation).findById(collectionId, BatchExecutionCollection.class);
	}
	
	@Test
	public void testSaveExecutions() {
		BatchExecutionCollection collection = new BatchExecutionCollection();
		service.saveExecutions(collection);
		
		Mockito.verify(mongoOperation).save(collection);
	}
}
