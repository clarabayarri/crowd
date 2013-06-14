package com.crowdplatform.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class BatchExecutionCollectionTest {

	private BatchExecutionCollection collection = new BatchExecutionCollection();
	
	@Test
	public void testAddExecutionAssignsId() {
		List<Execution> executions = Lists.newArrayList(new Execution(), new Execution());
		collection.setExecutions(executions);
		Execution execution = new Execution();
		
		collection.addExecution(execution);
		
		assertEquals(3, execution.getId().intValue());
	}
}
