package com.crowdplatform.service;

import com.crowdplatform.model.User;
import java.util.List;

public interface UserService {

	public boolean usernameExists(String username);
	
	public void addUser(User user);
	
	public User getUser(String username);
	
	public void saveUser(User user);

	public List<User> listUsers();
	
	public void removeUser(String username);
	
}
