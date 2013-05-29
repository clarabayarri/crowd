package com.crowdplatform.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.stereotype.Component;

import com.crowdplatform.model.MapReduceResult;
import com.crowdplatform.model.Project;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class DataMinerImpl implements DataMiner {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public MapReduceResults<MapReduceResult> aggregateByDate(Project project) {
		BasicQuery query1 = new BasicQuery("{ projectId : '"+ project.getId() + "' }");
		return mongoTemplate.mapReduce(query1, "batchExecutionCollection", 
				"classpath:mapreduce/map_by_date.js", 
				"classpath:mapreduce/reduce_by_sum.js", 
				MapReduceResult.class);
	}
	
	public MapReduceResults<MapReduceResult> aggregateByField(Project project, String field) {
		BasicQuery query1 = new BasicQuery("{ projectId : '"+ project.getId() + "' }");
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("fieldName", field);
		MapReduceOptions options = new MapReduceOptions().scopeVariables(vars).verbose(true).outputTypeInline();
		return mongoTemplate.mapReduce(query1, "batchExecutionCollection", 
				"classpath:mapreduce/map_by_field.js", 
				"classpath:mapreduce/reduce_by_sum.js", 
				options, MapReduceResult.class);
	}
	
	public MapReduceResults<MapReduceResult> aggregateByMultivaluateField(Project project, String field) {
		BasicQuery query1 = new BasicQuery("{ projectId : '"+ project.getId() + "' }");
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("fieldName", field);
		MapReduceOptions options = new MapReduceOptions().scopeVariables(vars).verbose(true).outputTypeInline();
		return mongoTemplate.mapReduce(query1, "batchExecutionCollection", 
				"classpath:mapreduce/map_by_multivaluate_field.js", 
				"classpath:mapreduce/reduce_by_sum.js", 
				options, MapReduceResult.class);
	}
	
	public Map<Object, Object> aggregateByFieldWithIntegerSteps(Project project, String field) {
		MapReduceResults<MapReduceResult> results = aggregateByField(project, field);
		List<MapReduceResult> list = Lists.newArrayList(results);
		Map<Object, Object> ordered = Maps.newLinkedHashMap();
		
		if (!list.isEmpty()) {
			Collections.sort(list, new Comparator<MapReduceResult>() {
				@Override
				public int compare(MapReduceResult arg0, MapReduceResult arg1) {
					Integer value1 = ((Number) arg0.getId()).intValue();
					Integer value2 = ((Number) arg1.getId()).intValue();
					return value1.compareTo(value2);
				}});
			Integer minValue = ((Number) list.get(0).getId()).intValue();
			Integer maxValue = ((Number) list.get(list.size()-1).getId()).intValue();
			Integer step = Math.max(1, (int) Math.ceil((maxValue - minValue) / 18.0));
			
			for (int i = minValue; i <= minValue + 19*step; i += step) {
				ordered.put(i, 0);
			}
			for (MapReduceResult res : list) {
				Integer value = ((Number) res.getId()).intValue();
				Integer slot = value - ((value-minValue) % step);
				Integer count = (Integer) ordered.get(slot);
				count += ((Number) res.getValue()).intValue();
				ordered.put(slot, count);
			}
		}
		
		return ordered;
	}
}
