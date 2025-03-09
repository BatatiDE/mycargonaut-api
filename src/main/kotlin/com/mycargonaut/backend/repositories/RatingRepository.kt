package com.mycargonaut.backend.repositories

import com.mycargonaut.backend.entities.Rating
import org.springframework.data.jpa.repository.JpaRepository

interface RatingRepository : JpaRepository<Rating, Long> {
    fun findByUserIdAndTripIdAndVoterId(userId: Long, tripId: Long, voterId: Long): Rating?
    fun findByUserId(userId: Long): List<Rating>
}
