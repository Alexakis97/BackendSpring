package com.aed.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;


import com.aed.demo.entity.TempUser;


public interface TempUserRepository extends JpaRepository<TempUser,Integer> {

}
