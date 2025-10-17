package com.lumina.luminabackend.repository.payment;

import com.lumina.luminabackend.entity.payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

    Optional<PaymentMethod> findByMethodName(String methodName);

    boolean existsByMethodName(String methodName);

    List<PaymentMethod> findAllByOrderByMethodNameAsc();

    List<PaymentMethod> findByMethodNameContainingIgnoreCase(String searchTerm);

    @Query("SELECT pm, COUNT(p) as usageCount " +
            "FROM PaymentMethod pm " +
            "LEFT JOIN pm.payments p " +
            "WHERE p.status = 'PAID' " +
            "GROUP BY pm.paymentMethodId " +
            "ORDER BY usageCount DESC")
    List<Object[]> findMostUsedPaymentMethods();

    @Query("SELECT pm.paymentMethodId, pm.methodName, pm.description, " +
            "COUNT(p) as totalPayments, " +
            "COALESCE(SUM(CASE WHEN p.status = 'PAID' THEN p.amount ELSE 0 END), 0) as totalAmount " +
            "FROM PaymentMethod pm " +
            "LEFT JOIN pm.payments p " +
            "GROUP BY pm.paymentMethodId, pm.methodName, pm.description")
    List<Object[]> findPaymentMethodStatistics();

    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.methodName IN :methodNames")
    List<PaymentMethod> findByMethodNameIn(@Param("methodNames") List<String> methodNames);

    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.methodName IN ('Visa', 'Mastercard', 'Efectivo', 'Transferencia')")
    List<PaymentMethod> findCommonPaymentMethods();
}