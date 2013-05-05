package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.BatchExecutionCollection;

public interface BatchService {
    
    public BatchExecutionCollection getExecutions(String collectionId);
    
    public void saveExecutions(BatchExecutionCollection collection);
    
    public List<BatchExecutionCollection> listCollections();

	public void removeCollection(BatchExecutionCollection collection);
    
}
