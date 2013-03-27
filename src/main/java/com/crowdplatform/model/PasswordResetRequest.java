package com.crowdplatform.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PasswordResetRequest {

	@Id
	private Long id;
	
	@OneToOne
	private PlatformUser user;
	
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

	public PlatformUser getUser() {
		return user;
	}

	public void setUser(PlatformUser user) {
		this.user = user;
	}

	public Date getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(Date generationDate) {
		this.generationDate = generationDate;
	}
	
	

}
