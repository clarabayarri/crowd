package com.crowdplatform.util;

import java.util.Map;

import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.MapReduceResult;
import com.crowdplatform.model.Project;

public interface DataMiner {

	public MapReduceResults<MapReduceResult> aggregateByDate(Project project);
	
	public MapReduceResults<MapReduceResult> aggregateByField(Project project, String field);
	
	public MapReduceResults<MapReduceResult> aggregateByMultivaluateField(Project project, String field);
	
	public Map<Object, Object> aggregateByFieldWithIntegerSteps(Project project, String field);
	
	public MapReduceResults<MapReduceResult> aggregateByDate(Project project, Batch batch);
	
	public MapReduceResults<MapReduceResult> aggregateByField(Project project, Batch batch, String field);
	
	public MapReduceResults<MapReduceResult> aggregateByMultivaluateField(Project project, Batch batch, String field);
	
	public Map<Object, Object> aggregateByFieldWithIntegerSteps(Project project, Batch batch, String field);
}
