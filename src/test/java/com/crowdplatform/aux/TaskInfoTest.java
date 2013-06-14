package com.crowdplatform.aux;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.crowdplatform.model.Task;
import com.google.common.collect.Maps;

public class TaskInfoTest {

	private TaskInfo taskInfo;
	
	private static final Integer batchId = 3;
	private static final Integer taskId = 444;
	private static final Map<String, Object> contents = Maps.newHashMap();
	
	@Test
	public void testCreateFromTask() {
		Task task = new Task();
		task.setBatchId(batchId);
		task.setTaskId(taskId);
		task.setContents(contents);
		
		taskInfo = new TaskInfo(task);
		
		assertEquals(batchId, taskInfo.getBatchId());
		assertEquals(taskId, taskInfo.getTaskId());
		assertEquals(contents, taskInfo.getContents());
	}
}
