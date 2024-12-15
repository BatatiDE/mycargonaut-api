package com.mycargonaut.backend.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "bookings")
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    val trip: Trip,

    var status: String = "Booked", // Default booking status

    val createdAt: LocalDateTime = LocalDateTime.now()
)
