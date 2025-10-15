package com.lumina.luminabackend.repository.district;

import com.lumina.luminabackend.entity.district.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {

    @Query("SELECT d.districtId, d.districtName, " +
            "(SELECT vp.photoUrl FROM VenuePhoto vp WHERE vp.venue.venueId IN " +
            "(SELECT v.venueId FROM Venue v WHERE v.district.districtId = d.districtId AND v.status = 'AVAILABLE') " +
            "ORDER BY vp.photoId ASC LIMIT 1) as photoUrl, " +
            "COUNT(v.venueId) as venueCount " +
            "FROM District d " +
            "LEFT JOIN Venue v ON d.districtId = v.district.districtId " +
            "WHERE v.status = 'AVAILABLE' OR v.status IS NULL " +
            "GROUP BY d.districtId, d.districtName")
    List<Object[]> findDistrictsWithVenueCount();

    List<District> findByDistrictNameContainingIgnoreCase(String name);

    boolean existsByDistrictNameIgnoreCase(String districtName);

    List<District> findByOrderByDistrictNameAsc();
}