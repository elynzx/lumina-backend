package com.lumina.luminabackend.repository.venue;

import com.lumina.luminabackend.entity.venue.Venue;
import com.lumina.luminabackend.entity.venue.VenueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {


    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.district " +
            "LEFT JOIN FETCH v.photos " +
            "LEFT JOIN v.venueEventTypes vet " +
            "LEFT JOIN vet.eventType et " +
            "WHERE v.status = :availableStatus " +
            "AND (:districtId IS NULL OR v.district.districtId = :districtId) " +
            "AND (:eventTypeId IS NULL OR et.eventTypeId = :eventTypeId) " +
            "AND (:minCapacity IS NULL OR v.maxCapacity >= :minCapacity) " +
            "AND (:maxCapacity IS NULL OR v.maxCapacity < :maxCapacity) " +
            "AND (:minPrice IS NULL OR v.pricePerHour >= :minPrice) " +
            "AND (:maxPrice IS NULL OR v.pricePerHour <= :maxPrice) " +
            "ORDER BY v.venueId")
    List<Venue> findWithEventTypeFilter(
            @Param("availableStatus") VenueStatus availableStatus,
            @Param("districtId") Integer districtId,
            @Param("eventTypeId") Integer eventTypeId,
            @Param("minCapacity") Integer minCapacity,
            @Param("maxCapacity") Integer maxCapacity,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.district " +
            "LEFT JOIN FETCH v.photos " +
            "WHERE v.status = :availableStatus " +
            "AND (:districtId IS NULL OR v.district.districtId = :districtId) " +
            "AND (:minCapacity IS NULL OR v.maxCapacity >= :minCapacity) " +
            "AND (:maxCapacity IS NULL OR v.maxCapacity < :maxCapacity) " +
            "AND (:minPrice IS NULL OR v.pricePerHour >= :minPrice) " +
            "AND (:maxPrice IS NULL OR v.pricePerHour <= :maxPrice) " +
            "ORDER BY v.venueId")
    List<Venue> findWithoutEventTypeFilter(
            @Param("availableStatus") VenueStatus availableStatus,
            @Param("districtId") Integer districtId,
            @Param("minCapacity") Integer minCapacity,
            @Param("maxCapacity") Integer maxCapacity,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.venueEventTypes vet " +
            "LEFT JOIN FETCH vet.eventType " +
            "WHERE v.venueId IN :venueIds")
    List<Venue> loadEventTypesByVenueIds(@Param("venueIds") List<Integer> venueIds);

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.district " +
            "LEFT JOIN FETCH v.photos " +
            "WHERE v.status = :availableStatus " +
            "ORDER BY v.venueId")
    List<Venue> findAvailable(@Param("availableStatus") VenueStatus availableStatus);

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.district " +
            "LEFT JOIN FETCH v.photos " +
            "WHERE v.venueId = :id")
    Optional<Venue> findByIdWithDetails(@Param("id") Integer id);

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.venueEventTypes vet " +
            "LEFT JOIN FETCH vet.eventType " +
            "WHERE v.venueId = :id")
    Optional<Venue> findByIdWithEventTypes(@Param("id") Integer id);

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.district " +
            "LEFT JOIN FETCH v.photos " +
            "ORDER BY v.venueId")
    List<Venue> findAllWithDetails();

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.venueEventTypes vet " +
            "LEFT JOIN FETCH vet.eventType " +
            "ORDER BY v.venueId")
    List<Venue> findAllWithEventTypes();

    @Query("SELECT DISTINCT v FROM Venue v " +
            "LEFT JOIN FETCH v.district " +
            "LEFT JOIN FETCH v.photos " +
            "WHERE LOWER(v.venueName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Venue> findByVenueNameContainingIgnoreCase(@Param("name") String name);

    boolean existsByVenueNameIgnoreCase(String venueName);
}