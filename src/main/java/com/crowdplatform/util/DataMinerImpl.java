package com.crowdplatform.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.crowdplatform.aux.MapReduceResult;
import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Project;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class DataMinerImpl implements DataMiner {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Map<Object, Object> aggregateByField(Project project, String fieldName) {
		if (fieldName.equals("date")) return aggregateByDate(project);
		Field field = project.getField(fieldName);
		if (field != null) {
			if (field.getType().equals(Field.Type.INTEGER)) return aggregateByFieldWithIntegerSteps(project, fieldName);
			if (field.getType().equals(Field.Type.STRING)) return transform(aggregateByFieldInternal(project, fieldName));
			if (field.getType().equals(Field.Type.MULTIVALUATE_STRING)) 
				return aggregateByMultivaluateField(project, fieldName);
		}
		return null;
	}
	
	public Map<Object, Object> aggregateByField(Project project, Batch batch, String fieldName) {
		if (fieldName.equals("date")) return aggregateByDate(project, batch);
		Field field = project.getField(fieldName);
		if (field != null) {
			if (field.getType().equals(Field.Type.INTEGER)) return aggregateByFieldWithIntegerSteps(project, batch, fieldName);
			if (field.getType().equals(Field.Type.STRING)) return transform(aggregateByFieldInternal(project, fieldName));
			if (field.getType().equals(Field.Type.MULTIVALUATE_STRING)) 
				return aggregateByMultivaluateField(project, batch, fieldName);
		}
		return null;
	}
	
	private Map<Object, Object> aggregateByDate(Project project) {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(project.getId()));
		return aggregateByDate(query);
	}
	
	private Map<Object, Object> aggregateByDate(Project project, Batch batch) {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(project.getId()).and("batchId").is(batch.getId()));
		return aggregateByDate(query);
	}
	
	private Map<Object, Object> aggregateByDate(Query query) {
		return transform(mongoTemplate.mapReduce(query, "batchExecutionCollection", 
				"classpath:mapreduce/map_by_date.js", 
				"classpath:mapreduce/reduce_by_sum.js", 
				MapReduceResult.class));
	}
	
	private MapReduceResults<MapReduceResult> aggregateByFieldInternal(Project project, String field) {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(project.getId()));
		return aggregateByField(query, field);
	}
	
	private MapReduceResults<MapReduceResult> aggregateByFieldInternal(Project project, Batch batch, String field) {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(project.getId()).and("batchId").is(batch.getId()));
		return aggregateByField(query, field);
	}
	
	private MapReduceResults<MapReduceResult> aggregateByField(Query query, String field) {
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("fieldName", field);
		MapReduceOptions options = new MapReduceOptions().scopeVariables(vars).verbose(true).outputTypeInline();
		return mongoTemplate.mapReduce(query, "batchExecutionCollection", 
				"classpath:mapreduce/map_by_field.js", 
				"classpath:mapreduce/reduce_by_sum.js", 
				options, MapReduceResult.class);
	}
	
	public Map<Object, Object> aggregateByMultivaluateField(Project project, String field) {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(project.getId()));
		return transform(aggregateByMultivaluateField(query, field));
	}
	
	public Map<Object, Object> aggregateByMultivaluateField(Project project, Batch batch, String field) {
		Query query = new Query();
		query.addCriteria(Criteria.where("projectId").is(project.getId()).and("batchId").is(batch.getId()));
		return transform(aggregateByMultivaluateField(query, field));
	}
	
	private MapReduceResults<MapReduceResult> aggregateByMultivaluateField(Query query, String field) {
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("fieldName", field);
		MapReduceOptions options = new MapReduceOptions().scopeVariables(vars).verbose(true).outputTypeInline();
		return mongoTemplate.mapReduce(query, "batchExecutionCollection", 
				"classpath:mapreduce/map_by_multivaluate_field.js", 
				"classpath:mapreduce/reduce_by_sum.js", 
				options, MapReduceResult.class);
	}
	
	public Map<Object, Object> aggregateByFieldWithIntegerSteps(Project project, String field) {
		MapReduceResults<MapReduceResult> results = aggregateByFieldInternal(project, field);
		return formatIntoSteps(results);
	}
	
	public Map<Object, Object> aggregateByFieldWithIntegerSteps(Project project, Batch batch, String field) {
		MapReduceResults<MapReduceResult> results = aggregateByFieldInternal(project, batch, field);
		return formatIntoSteps(results);
	}
	
	private Map<Object, Object> formatIntoSteps(MapReduceResults<MapReduceResult> results) {
		Map<Object, Object> ordered = Maps.newLinkedHashMap();
		
		if (results != null && results.getCounts().getOutputCount() != 0) {
			List<MapReduceResult> list = Lists.newArrayList(results);
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
	
	private Map<Object, Object> transform(MapReduceResults<MapReduceResult> results) {
		Map<Object, Object> map = Maps.newLinkedHashMap();
		if (results != null) {
			for (MapReduceResult result : results) {
				map.put(result.getId(), result.getValue());
			}
		}
		return map;
	}
}
