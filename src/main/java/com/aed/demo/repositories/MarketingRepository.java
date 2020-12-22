package com.aed.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.MarketingObject;

public interface MarketingRepository extends JpaRepository<MarketingObject,Integer>{

	//Optional<AED> findByAED_id(int AED_id);
	
}
