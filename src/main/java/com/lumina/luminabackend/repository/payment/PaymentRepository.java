package com.lumina.luminabackend.repository.payment;

import com.lumina.luminabackend.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}