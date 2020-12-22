package com.aed.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aed.demo.entity.User;
import com.aed.demo.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
    UserRepository userRepository;
	

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	
    	
        java.util.Optional<User> user = userRepository.findByUsername(userName);
        System.out.println(user.toString());
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

        return user.map(MyUserDetails::new).get();
    }
	
}
