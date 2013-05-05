package com.crowdplatform.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlatformUser {

	@Id
	private String username;
	
	private String password;
	
	private String email;
	
	private PasswordResetRequest passwordResetRequest;

	public PlatformUser() {
		
	}
	
	public PlatformUser(Registration registration) {
		this.username = registration.getUsername();
		this.password = registration.getPassword();
		this.email = registration.getEmail();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PasswordResetRequest getPasswordResetRequest() {
		return passwordResetRequest;
	}

	public void setPasswordResetRequest(PasswordResetRequest passwordResetRequest) {
		this.passwordResetRequest = passwordResetRequest;
	}

}
