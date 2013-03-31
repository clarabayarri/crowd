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
	
	private PasswordResetRequest request = new PasswordResetRequest();
	private static final Long requestId = Long.MIN_VALUE;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(em.find(PasswordResetRequest.class, requestId)).thenReturn(request);
	}
	
	@Test
	public void testAddRequest() {
		request.setUser(new PlatformUser());
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createQuery(Mockito.anyString())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Lists.newArrayList());
		
		service.addRequest(request);
		
		assertNotNull(request.getId());
		Mockito.verify(em).persist(request);
	}
	
	@Test
	public void testRemoveRequest() {
		service.removeRequest(request);
		
		Mockito.verify(em).remove(request);
	}
	
	@Test
	public void testGetRequest() {
		service.getRequest(requestId);
		
		Mockito.verify(em).find(PasswordResetRequest.class, requestId);
	}
	
	@Test
	public void testListRequests() {
		Query query = Mockito.mock(Query.class);
		Mockito.when(em.createQuery("FROM PasswordResetRequest")).thenReturn(query);
		
		service.listRequests();
		
		Mockito.verify(query).getResultList();
	}
}
