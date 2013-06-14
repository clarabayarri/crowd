package com.crowdplatform.util;


import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Query;

import com.crowdplatform.aux.MapReduceResult;
import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Field;
import com.crowdplatform.model.Project;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class DataMinerImplTest {

	@InjectMocks
	private DataMinerImpl dataMiner = new DataMinerImpl();
	
	@Mock
	private MongoTemplate mongoTemplate;
	
	private Project project;
	
	private Batch batch = new Batch();
	
	@Before
	public void setUp() {
		project = new Project();
		Field field1 = new Field();
		field1.setType(Field.Type.INTEGER);
		field1.setName("integer");
		Field field2 = new Field();
		field2.setType(Field.Type.STRING);
		field2.setName("string");
		Field field3 = new Field();
		field3.setType(Field.Type.MULTIVALUATE_STRING);
		field3.setName("multivaluate");
		List<Field> fields = Lists.newArrayList(field1, field2, field3);
		project.setOutputFields(fields);
	}
	
	@Test
	public void testAggregateByFieldDate() {
		dataMiner.aggregateByField(project, "date");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_date.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"), 
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByIntegerField() {
		dataMiner.aggregateByField(project, "integer");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_field.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"),
				Mockito.any(MapReduceOptions.class),
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByStringField() {
		dataMiner.aggregateByField(project, "string");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_field.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"),
				Mockito.any(MapReduceOptions.class),
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByMultivaluateField() {
		dataMiner.aggregateByField(project, "multivaluate");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_multivaluate_field.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"),
				Mockito.any(MapReduceOptions.class),
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByUnknownField() {
		Map<Object, Object> result = dataMiner.aggregateByField(project, "unknown");
		
		assertNull(result);
	}
	
	@Test
	public void testAggregateByUnprocessableFieldType() {
		Field field4 = new Field();
		field4.setType(Field.Type.BOOL);
		field4.setName("bool");
		project.addOutputField(field4);
		
		Map<Object, Object> result = dataMiner.aggregateByField(project, "bool");
		
		assertNull(result);
	}
	
	@Test
	public void testAggregateByBatchFieldDate() {
		dataMiner.aggregateByField(project, batch, "date");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_date.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"), 
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByBatchIntegerField() {
		dataMiner.aggregateByField(project, batch, "integer");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_field.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"),
				Mockito.any(MapReduceOptions.class),
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByBatchStringField() {
		dataMiner.aggregateByField(project, batch, "string");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_field.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"),
				Mockito.any(MapReduceOptions.class),
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateByBatchMultivaluateField() {
		dataMiner.aggregateByField(project, batch, "multivaluate");
		
		Mockito.verify(mongoTemplate).mapReduce(Mockito.any(Query.class), 
				Mockito.eq("batchExecutionCollection"), 
				Mockito.eq("classpath:mapreduce/map_by_multivaluate_field.js"), 
				Mockito.eq("classpath:mapreduce/reduce_by_sum.js"),
				Mockito.any(MapReduceOptions.class),
				Mockito.eq(MapReduceResult.class));
	}
	
	@Test
	public void testAggregateBatchByUnknownField() {
		Map<Object, Object> result = dataMiner.aggregateByField(project, batch, "unknown");
		
		assertNull(result);
	}
	
	@Test
	public void testAggregateBatchByUnprocessableFieldType() {
		Field field4 = new Field();
		field4.setType(Field.Type.BOOL);
		field4.setName("bool");
		project.addOutputField(field4);
		
		Map<Object, Object> result = dataMiner.aggregateByField(project, batch, "bool");
		
		assertNull(result);
	}
}
