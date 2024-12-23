package com.mycargonaut.backend.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "trips")
data class Trip(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    val driver: User,

    val startPoint: String,
    val destinationPoint: String,
    val date: String,
    val time: String,

    // Total capacity of the trip
    val totalCapacity: Int,

    // Available space (mutable for booking updates)
    var availableSpace: Int,

    // Status of the trip
    @Enumerated(EnumType.STRING)
    var status: TripStatus = TripStatus.SCHEDULED,

    @ManyToMany
    @JoinTable(
        name = "trip_bookings",
        joinColumns = [JoinColumn(name = "trip_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val bookedUsers: MutableList<User> = mutableListOf(),

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}

// Enum for trip status
enum class TripStatus {
    SCHEDULED,
    ONGOING,
    COMPLETED,
    CANCELED
}
