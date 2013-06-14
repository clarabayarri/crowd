package com.crowdplatform.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.crowdplatform.aux.Registration;

public class PlatformUserTest {

	private PlatformUser user;
	
	private static final String username = "username";
	private static final String password = "password";
	private static final String email = "email";
	
	@Test
	public void testCreateFromRegistrationReadsValues() {
		Registration registration = new Registration();
		registration.setUsername(username);
		registration.setPassword(password);
		registration.setEmail(email);
		
		user = new PlatformUser(registration);
		
		assertEquals(username, user.getUsername());
		assertEquals(password, user.getPassword());
		assertEquals(email, user.getEmail());
	}
}
