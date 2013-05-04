package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.Batch;

public interface BatchService {

	/**
	 * Persist a new batch.
	 * @param batch
	 */
	public void addBatch(Batch batch);
	
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
     * Persist changes to an existing batch
     * @param batch The batch to be saved
     */
    public void saveBatch(Batch batch);
	
    /**
     * Retrieve all ids for batches in the system with a RUNNING status for a given project.
     * @param projectId The project id
     * @return List with the ids for the batches with status RUNNING
     */
    public List<Integer> listRunningBatchIds(String projectId);
	
    /**
     * Retrieve all ids for batches in the system with a COMPLETED status for a given project.
     * @param projectId The project id
     * @return List with the ids for the batches with status COMPLETED
     */
    public List<Integer> listCompletedBatchIds(String projectId);
    
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
     * @return List with all the tasks from the batch identified by id with their executions loaded
     */
    public Batch getBatchWithTasksWithExecutions(Integer id);
}
