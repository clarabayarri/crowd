package com.crowdplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

	@Autowired
	MongoOperations mongoOperation;
}
