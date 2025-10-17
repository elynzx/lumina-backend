package com.lumina.luminabackend.repository.venue;

import com.lumina.luminabackend.entity.venue.VenueEventType;
import com.lumina.luminabackend.entity.venue.VenueEventTypeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueEventTypeRepository extends JpaRepository<VenueEventType, VenueEventTypeId> {

    List<VenueEventType> findByVenueId(Integer venueId);

    List<VenueEventType> findByEventTypeId(Integer eventTypeId);

    @Query("SELECT vet.eventType.eventTypeName FROM VenueEventType vet WHERE vet.venueId = :venueId")
    List<String> findEventTypeNamesByVenueId(@Param("venueId") Integer venueId);

    @Query("SELECT vet.venue.venueId FROM VenueEventType vet WHERE vet.eventTypeId = :eventTypeId")
    List<Integer> findVenueIdsByEventTypeId(@Param("eventTypeId") Integer eventTypeId);

    boolean existsByVenueIdAndEventTypeId(Integer venueId, Integer eventTypeId);
}
