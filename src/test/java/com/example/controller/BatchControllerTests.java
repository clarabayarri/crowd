package com.example.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

public class BatchControllerTests {

	@Test
	public void testHandleRequestView() {
		BatchController controller = new BatchController();
		String result = controller.listBatches(new HashMap<String, Object>());
		assertEquals("batches", result);
	}

}
