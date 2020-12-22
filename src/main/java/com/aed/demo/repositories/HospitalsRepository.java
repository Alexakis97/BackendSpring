package com.aed.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.Hospital;

public interface HospitalsRepository extends JpaRepository<Hospital,Integer>{

	//Optional<AED> findByAED_id(int AED_id);
	
}
