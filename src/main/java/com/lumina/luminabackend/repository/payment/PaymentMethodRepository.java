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

    // Buscar por nombre del método
    Optional<PaymentMethod> findByMethodName(String methodName);

    // Verificar si existe un método por nombre
    boolean existsByMethodName(String methodName);

    // Métodos de pago ordenados por nombre
    List<PaymentMethod> findAllByOrderByMethodNameAsc();

    // Buscar métodos que contengan un término en el nombre
    List<PaymentMethod> findByMethodNameContainingIgnoreCase(String searchTerm);

    // Métodos de pago más usados
    @Query("SELECT pm, COUNT(p) as usageCount " +
            "FROM PaymentMethod pm " +
            "LEFT JOIN pm.payments p " +
            "WHERE p.status = 'PAID' " +
            "GROUP BY pm.paymentMethodId " +
            "ORDER BY usageCount DESC")
    List<Object[]> findMostUsedPaymentMethods();

    // Métodos de pago con estadísticas
    @Query("SELECT pm.paymentMethodId, pm.methodName, pm.description, " +
            "COUNT(p) as totalPayments, " +
            "COALESCE(SUM(CASE WHEN p.status = 'PAID' THEN p.amount ELSE 0 END), 0) as totalAmount " +
            "FROM PaymentMethod pm " +
            "LEFT JOIN pm.payments p " +
            "GROUP BY pm.paymentMethodId, pm.methodName, pm.description")
    List<Object[]> findPaymentMethodStatistics();

    // Buscar métodos específicos por nombres
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.methodName IN :methodNames")
    List<PaymentMethod> findByMethodNameIn(@Param("methodNames") List<String> methodNames);

    // Métodos de pago para datos iniciales comunes
    @Query("SELECT pm FROM PaymentMethod pm WHERE pm.methodName IN ('Visa', 'Mastercard', 'Efectivo', 'Transferencia')")
    List<PaymentMethod> findCommonPaymentMethods();
}