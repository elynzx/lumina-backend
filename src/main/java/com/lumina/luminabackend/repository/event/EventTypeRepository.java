package com.lumina.luminabackend.repository.event;

import com.lumina.luminabackend.entity.event.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Integer> {

    List<EventType> findByEventTypeNameContainingIgnoreCase(String name);

    boolean existsByEventTypeNameIgnoreCase(String eventTypeName);
}