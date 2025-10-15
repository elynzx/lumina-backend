package com.lumina.luminabackend.repository.reservation;

import com.lumina.luminabackend.entity.reservation.ReservationFurniture;
import com.lumina.luminabackend.entity.reservation.ReservationFurnitureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReservationFurnitureRepository extends JpaRepository<ReservationFurniture, ReservationFurnitureId> {

    List<ReservationFurniture> findByReservationId(Integer reservationId);

    List<ReservationFurniture> findByFurnitureId(Integer furnitureId);

    @Query("SELECT SUM(rf.subtotal) FROM ReservationFurniture rf WHERE rf.reservationId = :reservationId")
    BigDecimal calculateTotalFurnitureCostByReservation(@Param("reservationId") Integer reservationId);

    @Query("SELECT rf.quantity FROM ReservationFurniture rf " +
            "WHERE rf.reservationId = :reservationId AND rf.furnitureId = :furnitureId")
    Integer findQuantityByReservationAndFurniture(
            @Param("reservationId") Integer reservationId,
            @Param("furnitureId") Integer furnitureId
    );

    @Query("SELECT rf.furnitureId, f.furnitureName, SUM(rf.quantity) as totalUsed " +
            "FROM ReservationFurniture rf " +
            "JOIN Furniture f ON rf.furnitureId = f.furnitureId " +
            "GROUP BY rf.furnitureId, f.furnitureName " +
            "ORDER BY totalUsed DESC")
    List<Object[]> findMostUsedFurniture();

    boolean existsByReservationIdAndFurnitureId(Integer reservationId, Integer furnitureId);

    void deleteByReservationId(Integer reservationId);

    @Query("SELECT rf.furnitureId, f.furnitureName, rf.quantity, rf.unitPrice, rf.subtotal, f.photoUrl " +
            "FROM ReservationFurniture rf " +
            "JOIN Furniture f ON rf.furnitureId = f.furnitureId " +
            "WHERE rf.reservationId = :reservationId")
    List<Object[]> findFurnitureDetailsByReservation(@Param("reservationId") Integer reservationId);

    @Query("SELECT SUM(rf.quantity) FROM ReservationFurniture rf " +
            "JOIN Reservation r ON rf.reservationId = r.reservationId " +
            "WHERE rf.furnitureId = :furnitureId " +
            "AND r.reservationDate = :date " +
            "AND r.status != 'CANCELLED'")
    Integer findUsedQuantityByFurnitureAndDate(
            @Param("furnitureId") Integer furnitureId,
            @Param("date") java.time.LocalDate date
    );
}