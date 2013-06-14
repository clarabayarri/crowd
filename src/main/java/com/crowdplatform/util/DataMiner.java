package com.crowdplatform.util;

import java.util.Map;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Project;

public interface DataMiner {

	public Map<Object, Object> aggregateByField(Project project, String fieldName);
	
	public Map<Object, Object> aggregateByField(Project project, Batch batch, String fieldName);
	
}
