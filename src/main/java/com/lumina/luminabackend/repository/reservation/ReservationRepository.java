package com.lumina.luminabackend.repository.reservation;

import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.venue.venueId = :venueId " +
            "AND r.reservationDate = :date " +
            "AND r.status != 'CANCELLED' " +
            "AND ((r.startTime <= :startTime AND r.endTime > :startTime) " +
            "OR (r.startTime < :endTime AND r.endTime >= :endTime) " +
            "OR (r.startTime >= :startTime AND r.endTime <= :endTime))")
    List<Reservation> findConflictingReservations(
            @Param("venueId") Integer venueId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<Reservation> findByUserOrderByCreatedAtDesc(User user);

    List<Reservation> findByUserAndStatusOrderByReservationDateDesc(User user, ReservationStatus status);

    List<Reservation> findByVenueVenueIdAndReservationDateOrderByStartTime(Integer venueId, LocalDate date);

    List<Reservation> findByStatusOrderByCreatedAtDesc(ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate BETWEEN :startDate AND :endDate " +
            "ORDER BY r.reservationDate, r.startTime")
    List<Reservation> findReservationsBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    long countByUser(User user);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.venue.venueId = :venueId " +
            "AND r.reservationDate = :date " +
            "AND r.status != 'CANCELLED' " +
            "AND ((r.startTime <= :startTime AND r.endTime > :startTime) " +
            "OR (r.startTime < :endTime AND r.endTime >= :endTime) " +
            "OR (r.startTime >= :startTime AND r.endTime <= :endTime))")
    boolean existsConflictingReservation(
            @Param("venueId") Integer venueId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("SELECT r FROM Reservation r WHERE r.user = :user " +
            "AND r.reservationDate >= CURRENT_DATE " +
            "AND r.status != 'CANCELLED' " +
            "ORDER BY r.reservationDate, r.startTime")
    List<Reservation> findUpcomingReservationsByUser(@Param("user") User user);

    @Query("SELECT r FROM Reservation r WHERE YEAR(r.reservationDate) = :year " +
            "AND MONTH(r.reservationDate) = :month " +
            "ORDER BY r.reservationDate")
    List<Reservation> findReservationsByMonth(
            @Param("year") int year,
            @Param("month") int month
    );

    List<Reservation> findByVenueVenueIdOrderByReservationDateDesc(Integer venueId);

    // Métodos para Admin
    List<Reservation> findAllByOrderByCreatedAtDesc();

    long countByStatus(ReservationStatus status);

    long countByVenueVenueIdAndStatus(Integer venueId, ReservationStatus status);

    long countByReservationDate(LocalDate date);

    @Query("SELECT COALESCE(SUM(r.totalCost), 0) FROM Reservation r " +
            "WHERE YEAR(r.reservationDate) = :year " +
            "AND MONTH(r.reservationDate) = :month " +
            "AND r.status = :status")
    BigDecimal sumTotalAmountByYearAndMonthAndStatus(
            @Param("year") int year,
            @Param("month") int month,
            @Param("status") ReservationStatus status
    );

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE YEAR(r.reservationDate) = :year " +
            "AND MONTH(r.reservationDate) = :month")
    long countByYearAndMonth(
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("SELECT v.venueName, COUNT(r) as reservationCount " +
            "FROM Reservation r JOIN r.venue v " +
            "GROUP BY v.venueId, v.venueName " +
            "ORDER BY reservationCount DESC")
    List<Object[]> findTopVenuesByReservationCount();

    @Query("SELECT f.furnitureName, " +
            "CASE " +
            "WHEN LOWER(f.furnitureName) LIKE '%silla%' OR LOWER(f.furnitureName) LIKE '%mesa%' " +
            "THEN COUNT(DISTINCT rf.reservation.reservationId) " +
            "WHEN LOWER(f.furnitureName) LIKE '%catering%' " +
            "THEN COUNT(DISTINCT rf.reservation.reservationId) " +
            "ELSE SUM(rf.quantity) END as totalQuantity " +
            "FROM ReservationFurniture rf JOIN rf.furniture f " +
            "GROUP BY f.furnitureId, f.furnitureName " +
            "ORDER BY " +
            "CASE " +
            "WHEN LOWER(f.furnitureName) LIKE '%silla%' OR LOWER(f.furnitureName) LIKE '%mesa%' " +
            "THEN COUNT(DISTINCT rf.reservation.reservationId) " +
            "WHEN LOWER(f.furnitureName) LIKE '%catering%' " +
            "THEN COUNT(DISTINCT rf.reservation.reservationId) " +
            "ELSE SUM(rf.quantity) END DESC")
    List<Object[]> findTopFurnitureByRequestCount();

    @Query("SELECT et.eventTypeName, COUNT(r) as reservationCount " +
            "FROM Reservation r JOIN r.eventType et " +
            "GROUP BY et.eventTypeId, et.eventTypeName " +
            "ORDER BY reservationCount DESC")
    List<Object[]> findTopEventTypesByReservationCount();

    List<Reservation> findByVenueVenueIdAndStatusIn(Integer venueId, List<ReservationStatus> statuses);

    @Query("SELECT COALESCE(SUM(r.totalCost), 0) FROM Reservation r " +
            "WHERE r.reservationDate BETWEEN :startDate AND :endDate " +
            "AND r.status = :status")
    default BigDecimal sumTotalAmountByDateRangeAndStatus(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ReservationStatus status
    ) {
        return null;
    }

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.reservationDate BETWEEN :startDate AND :endDate")
    long countByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(r.totalCost), 0) FROM Reservation r " +
            "WHERE DATE(r.createdAt) BETWEEN :startDate AND :endDate " +
            "AND r.status = :status")
    BigDecimal sumTotalAmountByCreatedAtRangeAndStatus(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") ReservationStatus status
    );

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE DATE(r.createdAt) BETWEEN :startDate AND :endDate")
    long countByCreatedAtRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}