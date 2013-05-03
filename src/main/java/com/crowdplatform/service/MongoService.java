package com.crowdplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.DB;

@Service
public class MongoService {

	@Autowired
	DB mongoDB;
}
