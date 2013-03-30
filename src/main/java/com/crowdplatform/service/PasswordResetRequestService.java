package com.crowdplatform.service;

import java.util.List;

import com.crowdplatform.model.PasswordResetRequest;

public interface PasswordResetRequestService {

	public void addRequest(PasswordResetRequest request);
	
	public PasswordResetRequest getRequest(Long id);
	
	public void removeRequest(PasswordResetRequest request);
	
	public List<PasswordResetRequest> listRequests();

}
