package com.lumina.luminabackend.repository.reservation;

import com.lumina.luminabackend.entity.reservation.Reservation;
import com.lumina.luminabackend.entity.reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByUser_UserIdOrderByReservationDateDesc(Integer userId);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByVenue_VenueIdOrderByReservationDateDesc(Integer venueId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.venue.venueId = :venueId " +
            "AND r.reservationDate = :date " +
            "AND r.status != 'CANCELLED' " +
            "AND ((:startTime BETWEEN r.startTime AND r.endTime) " +
            "     OR (:endTime BETWEEN r.startTime AND r.endTime) " +
            "     OR (r.startTime >= :startTime AND r.endTime <= :endTime))")
    List<Reservation> findConflictingReservations(
            @Param("venueId") Integer venueId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<Reservation> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT MONTH(r.reservationDate) as month, COUNT(r) as count " +
            "FROM Reservation r " +
            "WHERE YEAR(r.reservationDate) = :year " +
            "GROUP BY MONTH(r.reservationDate)")
    List<Object[]> findReservationsByMonth(@Param("year") Integer year);
}