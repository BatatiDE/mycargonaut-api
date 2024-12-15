package com.mycargonaut.backend.repositories

import com.mycargonaut.backend.entities.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : JpaRepository<Booking, Long> {
    fun findByTripId(tripId: Long): List<Booking>
}
