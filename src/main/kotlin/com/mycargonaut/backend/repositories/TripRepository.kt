package com.mycargonaut.backend.repositories

import com.mycargonaut.backend.entities.Trip
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TripRepository : JpaRepository<Trip, Long>
