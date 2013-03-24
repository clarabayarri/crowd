package com.crowdplatform.service;

import static org.junit.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.ProjectUser;

@RunWith(MockitoJUnitRunner.class)
public class ProjectUserServiceImplTest {

	@InjectMocks
	private ProjectUserService service = new ProjectUserServiceImpl();
	
	@Mock
	private EntityManager em;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddProjectUser() {
		ProjectUser user = new ProjectUser();
		
		service.addProjectUser(user);
		
		Mockito.verify(em).persist(user);
	}
	
	@Test
	public void testGetProjectUser() {
		String username = "username";
		ProjectUser user = new ProjectUser();
		Mockito.when(em.find(ProjectUser.class, username)).thenReturn(user);
		
		ProjectUser result = service.getProjectUser(username);
		
		Mockito.verify(em).find(ProjectUser.class, username);
		assertEquals(user, result);
	}

}
