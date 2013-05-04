package com.crowdplatform.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
	
	@Bean
	public DB getDb() throws UnknownHostException, MongoException {
		MongoURI mongoURI = getMongoURI();
        DB db = mongoURI.connectDB();
        db.authenticate(mongoURI.getUsername(), mongoURI.getPassword());
    
        return db;
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public Mongo mongo() throws UnknownHostException {
	return new Mongo(getMongoURI());
	}
	 
	@Override
	public String getDatabaseName() {
	return getMongoURI().getDatabase();
	}
	 
	@Bean
	public SimpleMongoDbFactory mongoDbFactory() throws Exception {
	return new SimpleMongoDbFactory(getMongoURI());
	}
	 
	@SuppressWarnings("deprecation")
	private MongoURI getMongoURI() {
	return new MongoURI(System.getenv("MONGOHQ_URL"));
	}

}
