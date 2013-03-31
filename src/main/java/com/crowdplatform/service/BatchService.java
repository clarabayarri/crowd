package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Batch;
import com.crowdplatform.model.Execution;

public interface BatchService {

	/**
	 * Save an existing batch.
	 * @param batch
	 */
    public void saveBatch(Batch batch);
    
    /**
     * Remove an existing batch.
     * @param id The batch id
     */
    public void removeBatch(Integer id);
    
    /**
     * Retrieve a persisted batch.
     * @param id The batch id
     * @return Batch corresponding to id
     */
    public Batch getBatch(Integer id);
	
    /**
     * Retrieve all ids for batches in the system with a RUNNING status.
     * @return List with the ids for the batches with status RUNNING
     */
    public List<Integer> listRunningBatchIds();
    
    /**
     * Set a batch's status to RUNNING.
     * @param id The batch id
     */
    public void startBatch(Integer id);
    
    /**
     * Set a batch's status to PAUSED.
     * @param id The batch id
     */
    public void pauseBatch(Integer id);
    
    /**
     * Retrieve all executions associated to a batch's tasks.
     * @param id The batch id
     * @return List with all the executions associated to tasks from the batch identified by id
     */
    public List<Execution> listExecutions(Integer id);
}
