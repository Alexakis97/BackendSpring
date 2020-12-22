package com.aed.demo.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.aed.demo.cache.CacheMemory;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired
	private CacheMemory myCache;
	
	@Autowired
	private HttpServletRequest request;

	public void onApplicationEvent(AuthenticationSuccessEvent ev) {
		
	

		String address = request.getRemoteAddr();
 
		if (myCache.isBlocked(address)) {
			throw new RuntimeException("blocked");
		}
		
		myCache.loginSucceeded(address);
	}
}
