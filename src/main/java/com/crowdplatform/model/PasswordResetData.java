package com.crowdplatform.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordResetData {

	@NotNull
	public Long uid;
	
	@Size(min = 4, max = 20, message="{registration.password.wrongSize}")
	public String password;
	
	public String confirmPassword;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
	
}
