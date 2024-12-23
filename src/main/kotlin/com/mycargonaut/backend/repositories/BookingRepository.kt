package com.mycargonaut.backend.repositories

import com.mycargonaut.backend.entities.Booking
import com.mycargonaut.backend.entities.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : JpaRepository<Booking, Long> {
    fun findByTrip(trip: Trip): List<Booking>
}
