package com.crowdplatform.util;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.BatchExecutionCollection;
import com.crowdplatform.model.Project;

public interface DataViewer {

	public String getDataURL(Project project, Batch batch, BatchExecutionCollection collection);
	
}
