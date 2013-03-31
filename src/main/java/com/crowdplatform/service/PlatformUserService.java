package com.crowdplatform.service;

import com.crowdplatform.model.PlatformUser;
import java.util.List;

public interface PlatformUserService {
	
	/**
	 * Persist a new user.
	 * @param user
	 */
	public void addUser(PlatformUser user);
	
	/**
	 * Save an existing user.
	 * @param user
	 */
	public void saveUser(PlatformUser user);
	
	/**
	 * Remove an existing user.
	 * @param username The user username
	 */
	public void removeUser(String username);
	
	/**
	 * Retrieve a persisted user.
	 * @param username The user username
	 * @return PlatformUser corresponding to username
	 */
	public PlatformUser getUser(String username);
	
	/**
	 * Retrieve a persisted user by username or email.
	 * @param username The user username or email
	 * @return PlatformUser corresponding to username or with email username
	 */
	public PlatformUser getUserByUsernameOrEmail(String username);

	/**
	 * Retrieve all users in the system.
	 * @return List with all PlatformUser in the system
	 */
	public List<PlatformUser> listUsers();

	/**
	 * Retrieve if a user with the provided username already exists.
	 * @param username The username to check
	 * @return True if there is a PlatformUser in the system with the provided username
	 */
	public boolean usernameExists(String username);
	
}
