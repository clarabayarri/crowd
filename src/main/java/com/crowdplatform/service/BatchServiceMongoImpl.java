package com.crowdplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.BatchExecutionCollection;

@Service
public class BatchServiceMongoImpl implements BatchService {

	@Autowired
	private MongoOperations mongoOperation;
	
	@Override
	public BatchExecutionCollection getExecutions(String collectionId) {
		return mongoOperation.findById(collectionId, BatchExecutionCollection.class);
	}

	@Override
	public void saveExecutions(BatchExecutionCollection collection) {
		mongoOperation.save(collection);
	}
}
