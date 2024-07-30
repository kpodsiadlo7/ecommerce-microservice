package com.example.cartservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface EventRepository extends JpaRepository<EventEntity, Long> {
    Optional<EventEntity> findByIdAndEventStatus(Long eventId, EventEntity.EventStatus status);
}
