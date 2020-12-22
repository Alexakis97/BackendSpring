package com.aed.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aed.demo.entity.Reports;

public interface ReportsRepository extends JpaRepository<Reports,Integer> {

}
