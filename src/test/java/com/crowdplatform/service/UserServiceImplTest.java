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

import com.crowdplatform.model.PlatformUser;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl service = new UserServiceImpl();

	@Mock
	private EntityManager em;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddUser() {
		PlatformUser user = new PlatformUser();
		
		service.addUser(user);
		
		Mockito.verify(em).persist(user);
	}
	
	@Test
	public void testRemoveUser() {
		String username = "username";
		PlatformUser user = new PlatformUser();
		Mockito.when(em.find(PlatformUser.class, username)).thenReturn(user);
		
		service.removeUser(username);
		
		Mockito.verify(em).remove(user);
	}
	
	@Test
	public void testSaveUser() {
		PlatformUser user = new PlatformUser();
		
		service.saveUser(user);
		
		Mockito.verify(em).merge(user);
	}
	
	@Test
	public void testUsernameExistsForTrue() {
		String username = "username";
		Mockito.when(em.find(PlatformUser.class, username)).thenReturn(new PlatformUser());
		
		boolean result = service.usernameExists(username);
		
		assertEquals(true, result);
	}
	
	@Test
	public void testUsernameExistsForFalse() {
		String username = "username";
		Mockito.when(em.find(PlatformUser.class, username)).thenReturn(null);
		
		boolean result = service.usernameExists(username);
		
		assertEquals(false, result);
	}
}
