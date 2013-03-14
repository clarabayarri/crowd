package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Execution;

public interface BatchService {

	public void addBatch(Batch batch);
	
    public List<Batch> listBatches();
    
    public void removeBatch(Integer id);
    
    public Batch getBatch(Integer id);
    
    public List<Integer> listRunningBatchIds();
    
    public void saveBatch(Batch batch);
    
    public void startBatch(Integer id);
    
    public void pauseBatch(Integer id);
    
    public void createBatch(Batch batch, Integer projectId);
    
    public List<Execution> listExecutions(Integer id);
}
