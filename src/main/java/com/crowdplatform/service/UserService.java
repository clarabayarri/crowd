package com.crowdplatform.service;

import com.crowdplatform.model.PlatformUser;
import java.util.List;

public interface UserService {

	public boolean usernameExists(String username);
	
	public void addUser(PlatformUser user);
	
	public PlatformUser getUser(String username);
	
	public void saveUser(PlatformUser user);

	public List<PlatformUser> listUsers();
	
	public void removeUser(String username);
	
}
