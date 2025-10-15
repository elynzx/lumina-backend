package com.lumina.luminabackend.repository.payment;

import com.lumina.luminabackend.entity.payment.Payment;
import com.lumina.luminabackend.entity.payment.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Pagos por reserva
    List<Payment> findByReservation_ReservationIdOrderByPaymentDateDesc(Integer reservationId);

    // Pagos por usuario (a través de la reserva)
    @Query("SELECT p FROM Payment p WHERE p.reservation.user.userId = :userId ORDER BY p.paymentDate DESC")
    List<Payment> findByUserIdOrderByPaymentDateDesc(@Param("userId") Integer userId);

    // Pagos por estado
    List<Payment> findByStatusOrderByPaymentDateDesc(PaymentStatus status);

    // Pagos pendientes
    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING'")
    List<Payment> findPendingPayments();

    // Pagos exitosos
    @Query("SELECT p FROM Payment p WHERE p.status = 'PAID'")
    List<Payment> findSuccessfulPayments();

    // Buscar por código de confirmación
    Optional<Payment> findByConfirmationCode(String confirmationCode);

    // Pagos por metodo de pago
    List<Payment> findByPaymentMethod_PaymentMethodId(Integer paymentMethodId);

    // Pagos en un rango de fechas
    List<Payment> findByPaymentDateBetweenOrderByPaymentDateDesc(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Total pagado por una reserva
    @Query("SELECT SUM(p.amount) FROM Payment p " +
            "WHERE p.reservation.reservationId = :reservationId AND p.status = 'PAID'")
    BigDecimal findTotalPaidByReservation(@Param("reservationId") Integer reservationId);

    // Verificar si una reserva está completamente pagada
    @Query("SELECT CASE WHEN (SELECT SUM(p.amount) FROM Payment p " +
            "WHERE p.reservation.reservationId = :reservationId AND p.status = 'PAID') " +
            ">= r.totalCost THEN true ELSE false END " +
            "FROM Reservation r WHERE r.reservationId = :reservationId")
    Boolean isReservationFullyPaid(@Param("reservationId") Integer reservationId);

    // Estadísticas: ingresos por mes
    @Query("SELECT MONTH(p.paymentDate) as month, YEAR(p.paymentDate) as year, SUM(p.amount) as total " +
            "FROM Payment p " +
            "WHERE p.status = 'PAID' AND YEAR(p.paymentDate) = :year " +
            "GROUP BY YEAR(p.paymentDate), MONTH(p.paymentDate) " +
            "ORDER BY year, month")
    List<Object[]> findMonthlyRevenue(@Param("year") Integer year);

    // Estadísticas: pagos por método
    @Query("SELECT pm.methodName, COUNT(p) as paymentCount, SUM(p.amount) as totalAmount " +
            "FROM Payment p " +
            "JOIN p.paymentMethod pm " +
            "WHERE p.status = 'PAID' " +
            "GROUP BY pm.methodName")
    List<Object[]> findPaymentStatsByMethod();

    // Pagos fallidos que necesitan atención
    @Query("SELECT p FROM Payment p " +
            "WHERE p.status = 'FAILED' " +
            "AND p.paymentDate >= :since " +
            "ORDER BY p.paymentDate DESC")
    List<Payment> findFailedPaymentsSince(@Param("since") LocalDateTime since);

    // Último pago de una reserva
    @Query("SELECT p FROM Payment p " +
            "WHERE p.reservation.reservationId = :reservationId " +
            "ORDER BY p.paymentDate DESC " +
            "LIMIT 1")
    Optional<Payment> findLatestPaymentByReservation(@Param("reservationId") Integer reservationId);
}