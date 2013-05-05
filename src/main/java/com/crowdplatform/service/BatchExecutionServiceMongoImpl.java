package com.crowdplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.BatchExecutionCollection;

@Service
public class BatchExecutionServiceMongoImpl implements BatchExecutionService {

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

	@Override
	public List<BatchExecutionCollection> listCollections() {
		return mongoOperation.findAll(BatchExecutionCollection.class);
	}

	@Override
	public void removeCollection(BatchExecutionCollection collection) {
		mongoOperation.remove(collection);
	}
}
