package com.example.service;

import java.util.List;

import com.example.model.Batch;

public interface BatchService {

	public void addBatch(Batch batch);
	
    public List<Batch> listBatches();
    
    public void removeBatch(Integer id);
    
    public Batch getBatch(Integer id);
    
    public void saveBatch(Batch batch);
}
