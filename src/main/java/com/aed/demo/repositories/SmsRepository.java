package com.aed.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.Sms;

public interface SmsRepository extends JpaRepository<Sms,Integer> {

}
