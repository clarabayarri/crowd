package com.crowdplatform.service;

import com.crowdplatform.model.PasswordResetRequest;

public interface PasswordResetRequestService {

	public void addRequest(PasswordResetRequest request);
	
	public PasswordResetRequest getRequest(Long id);
	
	public void removeRequest(PasswordResetRequest request);

}
