package com.crowdplatform.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crowdplatform.model.PlatformUser;

@Service
public class PlatformUserServiceImpl implements PlatformUserService {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void addUser(PlatformUser user) {
		em.persist(user);
	}

	@Transactional
	public void saveUser(PlatformUser user) {
		em.merge(user);
	}
	
	@Transactional
	public void removeUser(String username) {
		PlatformUser user = em.find(PlatformUser.class, username);
		if (user != null) {
			em.remove(user);
		}
	}

	@Transactional
	public PlatformUser getUser(String username) {
		return em.find(PlatformUser.class, username);
	}
	
	@Transactional
	public PlatformUser getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	String username = auth.getName();
		    return getUser(username);
	    }
	    return null;
	}
	
	@Transactional
	public PlatformUser getUserByUsernameOrEmail(String username) {
		PlatformUser user = getUser(username);
		if (user == null) {
			user = getUserByEmail(username);
		}
		return user;
	}
	
	@Transactional
	public PlatformUser getUserByEmail(String email) {
		Query query = em.createQuery("FROM PlatformUser WHERE email='" + email + "'");
		if (query.getResultList().size() > 0) {
			return (PlatformUser) query.getResultList().get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<PlatformUser> listUsers() {
		Query query = em.createQuery("FROM PlatformUser");
		return query.getResultList();
	}
	
	@Transactional
	public boolean usernameExists(String username) {
		PlatformUser user = em.find(PlatformUser.class, username);
		return user !=  null;
	}

	@Transactional
	public boolean currentUserIsAuthorizedForProject(Integer projectId) {
		PlatformUser user = getCurrentUser();
		if (user != null) {
			return getCurrentUser().isOwnerOfProject(projectId);
		}
	    return false;
	}

	@Transactional
	public boolean currentUserIsAuthorizedForBatch(Integer projectId,
			Integer batchId) {
		PlatformUser user = getCurrentUser();
		if (user != null) {
			return getCurrentUser().isOwnerOfBatch(projectId, batchId);
		}
	    return false;
	}

}
