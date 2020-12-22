package com.aed.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.AED;

public interface AEDRepository extends JpaRepository<AED,Integer>{

	//Optional<AED> findByAED_id(int AED_id);
	
}
