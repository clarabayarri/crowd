package com.crowdplatform.util;

import java.io.IOException;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Project;


public interface FileWriter {

	public String writeTasksExecutions(Project project, Batch batch, BatchExecutionCollection collection, Boolean header) throws IOException;
	
}
