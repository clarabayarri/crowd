package com.crowdplatform.model;

import java.util.Date;

public class PasswordResetRequest {

	private Long id;
	
	private Date generationDate;
	
	public PasswordResetRequest() {
		this.generationDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(Date generationDate) {
		this.generationDate = generationDate;
	}
	
	

}
