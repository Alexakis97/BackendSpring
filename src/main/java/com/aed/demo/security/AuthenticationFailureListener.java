package com.aed.demo.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.aed.demo.cache.CacheMemory;
import com.aed.demo.entity.User;
import com.aed.demo.repositories.UserRepository;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CacheMemory myCache;

	@Autowired
	private HttpServletRequest request;
	
	// @Autowired
	// private HttpServletResponse response;

	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent ev) {

		String username = ev.getAuthentication().getName();
		
		// WebAuthenticationDetails auth = (WebAuthenticationDetails)
		// ev.getAuthentication().getDetails();

		String address = request.getRemoteAddr();

		myCache.loginFailed(address);

		if (myCache.isBlocked(address)) {
			List<User> users = userRepo.findAll();
			User userInDanger = null;
			for (User user : users) {
				if (user.getUsername().equals(username)) {
					userInDanger = user;
				}
			}

			Mail mail = new Mail();
			mail.sendEmailOKMail(userInDanger.getEmail(), "LifeTime Warning Message", "Your account may be in danger",
					"Please contact the support department", "Ip :" + address, "Thank you for using Aed");
			throw new RuntimeException("blocked");

		}

	}

}
