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
public class PlatformUserServiceImplTest {

	@InjectMocks
	private PlatformUserServiceImpl service = new PlatformUserServiceImpl();

	@Mock
	private EntityManager em;
	
	private PlatformUser user = new PlatformUser();
	private static final String username = "username";
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(em.find(PlatformUser.class, username)).thenReturn(user);
	}
	
	@Test
	public void testAddUser() {
		service.addUser(user);
		
		Mockito.verify(em).persist(user);
	}
	
	@Test
	public void testSaveUser() {
		service.saveUser(user);
		
		Mockito.verify(em).merge(user);
	}
	
	@Test
	public void testRemoveUser() {
		service.removeUser(username);
		
		Mockito.verify(em).remove(user);
	}
	
	@Test
	public void testUsernameExistsForTrue() {
		boolean result = service.usernameExists(username);
		
		assertEquals(true, result);
	}
	
	@Test
	public void testUsernameExistsForFalse() {
		Mockito.when(em.find(PlatformUser.class, username)).thenReturn(null);
		
		boolean result = service.usernameExists(username);
		
		assertEquals(false, result);
	}
}
