package com.aed.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import com.aed.demo.entity.Payment;
import com.aed.demo.entity.User;


public interface PaymentRepository extends JpaRepository<Payment,Integer>{

	//Optional<PaymentRepository> findByUserUserId(int user_id);
}
