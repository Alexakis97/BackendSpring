package com.aed.demo.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.User;


public interface UserRepository extends JpaRepository<User,Integer> {
	
	Optional<User> findByUsername(String username);

}
