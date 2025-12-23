package com.lumina.luminabackend.repository.payment;

import com.lumina.luminabackend.entity.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {


}