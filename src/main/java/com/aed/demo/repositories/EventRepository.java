package com.aed.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.Event;


public interface EventRepository extends JpaRepository<Event,Integer> {

}
