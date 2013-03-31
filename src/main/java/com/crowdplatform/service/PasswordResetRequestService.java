package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.PasswordResetRequest;

public interface PasswordResetRequestService {

	/**
	 * Persist a new request.
	 * @param request
	 */
	public void addRequest(PasswordResetRequest request);
	
	/**
	 * Remove an existing request.
	 * @param request
	 */
	public void removeRequest(PasswordResetRequest request);
	
	/**
	 * Retrieve a persisted request
	 * @param id The request id
	 * @return PasswordResetRequest corresponding to id
	 */
	public PasswordResetRequest getRequest(Long id);
	
	/**
	 * Retrieve all requests in the system.
	 * @return List with all PasswordResetRequest in the system
	 */
	public List<PasswordResetRequest> listRequests();

}
