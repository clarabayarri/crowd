package com.crowdplatform.service;

import com.crowdplatform.model.ProjectUser;

public interface ProjectUserService {

	public void addProjectUser(ProjectUser user);
	
	public ProjectUser getProjectUser(Integer id);
	
	public ProjectUser getProjectUserByUsername(String username);

}
