package com.crowdplatform.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crowdplatform.model.PlatformUser;
import com.crowdplatform.service.PlatformUserService;
import com.google.common.collect.Lists;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private PlatformUserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		PlatformUser user = userService.getUser(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		GrantedAuthority auth = new GrantedAuthorityImpl("ROLE_PLATFORM_USER");
		List<GrantedAuthority> authorities = Lists.newArrayList(auth);
		User userDetail = new User(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
		return userDetail;
	}

}
