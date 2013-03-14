package com.crowdplatform.util;

import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import java.util.List;

import org.junit.Test;

import com.crowdplatform.model.Execution;
import com.google.common.collect.Lists;

public class FileWriterTest {

	private FileWriter writer = new FileWriter();

	@Test
	public void testWriteExecutionsReturnsStringWriter() {
		List<Execution> executions = Lists.newArrayList();
		StringWriter result = writer.writeExecutions(executions);
		
		assertNotNull(result);
	}
}
