package com.crowdplatform.service;

import com.crowdplatform.model.User;

public interface UserService {

	public boolean usernameExists(String username);
	
	public void addUser(User user);

}
