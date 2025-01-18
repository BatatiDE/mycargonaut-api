package com.mycargonaut.backend.repositories

import com.mycargonaut.backend.entities.Trip
import com.mycargonaut.backend.entities.TripStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TripRepository : JpaRepository<Trip, Long> {
    fun findByDateAfterAndStatusIn(date: LocalDateTime, statuses: List<TripStatus>): List<Trip>
}
