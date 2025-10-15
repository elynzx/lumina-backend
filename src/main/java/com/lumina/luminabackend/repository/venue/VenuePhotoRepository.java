package com.lumina.luminabackend.repository.venue;

import com.lumina.luminabackend.entity.venue.VenuePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenuePhotoRepository extends JpaRepository<VenuePhoto, Integer> {

    List<VenuePhoto> findByVenue_VenueId(Integer venueId);

    @Query("SELECT vp.photoUrl FROM VenuePhoto vp WHERE vp.venue.venueId = :venueId ORDER BY vp.photoId")
    List<String> findPhotoUrlsByVenueId(@Param("venueId") Integer venueId);

    @Query("SELECT vp FROM VenuePhoto vp WHERE vp.venue.venueId = :venueId ORDER BY vp.photoId LIMIT 1")
    Optional<VenuePhoto> findMainPhotoByVenueId(@Param("venueId") Integer venueId);

    @Query("SELECT vp.photoUrl FROM VenuePhoto vp WHERE vp.venue.venueId = :venueId ORDER BY vp.photoId LIMIT 1")
    Optional<String> findMainPhotoUrlByVenueId(@Param("venueId") Integer venueId);
}