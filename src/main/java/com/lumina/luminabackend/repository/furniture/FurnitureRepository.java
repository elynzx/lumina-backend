package com.lumina.luminabackend.repository.furniture;

import com.lumina.luminabackend.entity.furniture.Furniture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, Integer> {

    @Query("SELECT f FROM Furniture f WHERE f.totalStock > 0")
    List<Furniture> findAvailable();

    List<Furniture> findByFurnitureNameContainingIgnoreCase(String name);

    @Query("SELECT f.totalStock FROM Furniture f WHERE f.furnitureId = :furnitureId")
    Integer findAvailableStock(@Param("furnitureId") Integer furnitureId);

    @Query("SELECT COALESCE(SUM(rf.quantity), 0) FROM ReservationFurniture rf " +
            "JOIN Reservation r ON rf.reservationId = r.reservationId " +
            "WHERE rf.furnitureId = :furnitureId " +
            "AND r.reservationDate = :date " +
            "AND r.status != 'CANCELLED'")
    Integer findUsedQuantityByFurnitureAndDate(
            @Param("furnitureId") Integer furnitureId,
            @Param("date") LocalDate date
    );
}