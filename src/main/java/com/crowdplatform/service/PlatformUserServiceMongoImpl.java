package com.crowdplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.DefaultIndexOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.PlatformUser;

@Service
public class PlatformUserServiceMongoImpl implements PlatformUserService {

	@Autowired
	private MongoOperations mongoOperation;
	
	@Override
	public void addUser(PlatformUser user) {
		checkForEmailIndex();
		checkForPasswordResetRequestIndex();
		mongoOperation.save(user);
	}

	@Override
	public void saveUser(PlatformUser user) {
		mongoOperation.save(user);
	}

	@Override
	public void removeUser(String username) {
		PlatformUser user = getUser(username);
		if (user != null)
			mongoOperation.remove(user);
		else
			System.out.println("PlatformUserServiceMongoImpl: Could not remove user, user not found.");
	}

	@Override
	public PlatformUser getUser(String username) {
		return mongoOperation.findById(username, PlatformUser.class);
	}

	@Override
	public PlatformUser getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	String username = auth.getName();
		    return getUser(username);
	    }
	    return null;
	}

	@Override
	public PlatformUser getUserByUsernameOrEmail(String username) {
		PlatformUser user = getUser(username);
		if (user == null) {
			user = getUserByEmail(username);
		}
		return user;
	}
	
	private PlatformUser getUserByEmail(String email) {
		Query query = new Query(Criteria.where("email").is(email));
		return mongoOperation.findOne(query, PlatformUser.class);
	}

	@Override
	public List<PlatformUser> listUsers() {
		return mongoOperation.findAll(PlatformUser.class);
	}

	@Override
	public boolean usernameExists(String username) {
		PlatformUser user = getUser(username);
		return user != null;
	}

	@Override
	public PlatformUser userWithPasswordResetRequest(Long uid) {
		Query query = new Query(Criteria.where("passwordResetRequest.id").is(uid));
		return mongoOperation.findOne(query, PlatformUser.class);
	}
	
	private void checkForEmailIndex() {
		DefaultIndexOperations indexOperations = new DefaultIndexOperations(mongoOperation, "platformUser");
		indexOperations.ensureIndex(new Index().on("email", Order.ASCENDING).unique());
	}
	
	private void checkForPasswordResetRequestIndex() {
		DefaultIndexOperations indexOperations = new DefaultIndexOperations(mongoOperation, "platformUser");
		indexOperations.ensureIndex(new Index().on("passwordResetRequest.id", Order.ASCENDING).unique());
	}

}
