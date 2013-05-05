package com.crowdplatform.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest {

	private Project project;
	
	@Before
	public void setUp() {
		project = new Project();
	}
	
	@Test
	public void testGetRunningBatches() {
		Batch batch1 = new Batch();
		batch1.setState(Batch.State.RUNNING);
		project.addBatch(batch1);
		Batch batch2 = new Batch();
		batch2.setState(Batch.State.COMPLETE);
		project.addBatch(batch2);
		Batch batch3 = new Batch();
		batch3.setState(Batch.State.PAUSED);
		
		List<Batch> result = project.getRunningBatches();
		
		assertEquals(1, result.size());
		assertEquals(batch1, result.get(0));
	}
	
	@Test
	public void testGetCompletedBatches() {
		Batch batch1 = new Batch();
		batch1.setState(Batch.State.RUNNING);
		project.addBatch(batch1);
		Batch batch2 = new Batch();
		batch2.setState(Batch.State.COMPLETE);
		project.addBatch(batch2);
		Batch batch3 = new Batch();
		batch3.setState(Batch.State.PAUSED);
		
		List<Batch> result = project.getCompletedBatches();
		
		assertEquals(1, result.size());
		assertEquals(batch2, result.get(0));
	}
}
