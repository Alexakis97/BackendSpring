package com.aed.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.Ambulance;


public interface AmbulanceRepository extends JpaRepository<Ambulance,Integer> {

}
