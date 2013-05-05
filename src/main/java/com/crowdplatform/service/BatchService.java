package com.crowdplatform.service;

import com.crowdplatform.model.BatchExecutionCollection;

public interface BatchService {
    
    public BatchExecutionCollection getExecutions(String collectionId);
    
    public void saveExecutions(BatchExecutionCollection collection);
    
}
