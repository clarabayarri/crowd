package com.crowdplatform.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.crowdplatform.model.PlatformUser;

@RunWith(MockitoJUnitRunner.class)
public class PlatformUserServiceMongoImplTest {

	@InjectMocks
	private PlatformUserServiceMongoImpl service = new PlatformUserServiceMongoImpl();
	
	@Mock
	private MongoOperations mongoOperation;
	
	private static final String username = "username";
	
	@Before
	public void setUp() {
	    MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddUser() {
		PlatformUser user = new PlatformUser();
		
		service.addUser(user);
		
		Mockito.verify(mongoOperation).save(user);
	}
	
	@Test
	public void testSaveUser() {
		PlatformUser user = new PlatformUser();
		
		service.saveUser(user);
		
		Mockito.verify(mongoOperation).save(user);
	}
	
	@Test
	public void testRemoveExistingUser() {
		PlatformUser user = new PlatformUser();
		Mockito.when(mongoOperation.findById(username, PlatformUser.class)).thenReturn(user);
		
		service.removeUser(username);
		
		Mockito.verify(mongoOperation).remove(user);
	}
	
	@Test
	public void testRemoveNonExistingUser() {
		service.removeUser(username);
		
		Mockito.verify(mongoOperation, Mockito.never()).remove(Mockito.any(PlatformUser.class));
	}
	
	@Test
	public void testGetUser() {
		service.getUser(username);
		
		Mockito.verify(mongoOperation).findById(username, PlatformUser.class);
	}
	
	@Test
	public void testGetCurrentUserWithAuth() {
		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn(username);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		service.getCurrentUser();
		
		Mockito.verify(mongoOperation).findById(username, PlatformUser.class);
	}
	
	@Test
	public void testGetCurrentUserWithoutAuth() {
		SecurityContextHolder.getContext().setAuthentication(null);
		
		PlatformUser result = service.getCurrentUser();
		
		assertNull(result);
	}
	
	@Test
	public void testGetUserByUsernameOrEmail() {
		service.getUserByUsernameOrEmail("email");
		
		Mockito.verify(mongoOperation).findOne(Mockito.any(Query.class), Mockito.eq(PlatformUser.class));
	}
	
	@Test
	public void testListUsers() {
		service.listUsers();
		
		Mockito.verify(mongoOperation).findAll(PlatformUser.class);
	}
	
	@Test
	public void testUsernameExistsForFalse() {
		boolean result = service.usernameExists(username);
		
		assertEquals(false, result);
	}
	
	@Test
	public void testUsernameExistsForTrue() {
		Mockito.when(mongoOperation.findById(username, PlatformUser.class)).thenReturn(new PlatformUser());
		
		boolean result = service.usernameExists(username);
		
		assertEquals(true, result);
	}
	
	@Test
	public void testUserWithPasswordResetRequest() {
		service.userWithPasswordResetRequest(new Long(3));
		
		Mockito.verify(mongoOperation).findOne(Mockito.any(Query.class), Mockito.eq(PlatformUser.class));
	}
}
