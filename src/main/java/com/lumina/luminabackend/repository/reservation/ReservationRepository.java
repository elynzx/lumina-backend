package com.lumina.luminabackend.repository.reservation;

import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import com.lumina.luminabackend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}