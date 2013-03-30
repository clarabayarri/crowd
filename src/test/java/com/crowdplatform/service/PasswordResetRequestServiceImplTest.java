package com.crowdplatform.service;

import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.crowdplatform.model.PasswordResetRequest;
import com.crowdplatform.model.PlatformUser;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class PasswordResetRequestServiceImplTest {

	@InjectMocks
	private PasswordResetRequestServiceImpl service = new PasswordResetRequestServiceImpl();
	
	@Mock
	EntityManager em;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testAddRequest() {
		PasswordResetRequest request = new PasswordResetRequest();
		request.setUser(new PlatformUser());
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Lists.newArrayList());
		
		service.addRequest(request);
		
		assertNotNull(request.getId());
		Mockito.verify(em).persist(request);
	}
	
	@Test
	public void testGetRequest() {
		service.getRequest(Long.MIN_VALUE);
		
		Mockito.verify(em).find(PasswordResetRequest.class, Long.MIN_VALUE);
	}
	
	@Test
	public void testListRequests() {
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createQuery("FROM PasswordResetRequest")).thenReturn(query);
		
		service.listRequests();
		
		Mockito.verify(query).getResultList();
	}
}
