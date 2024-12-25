package com.mycargonaut.backend.repositories

import com.mycargonaut.backend.entities.Review
import com.mycargonaut.backend.entities.Trip
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByTrip(trip: Trip): List<Review>
}
