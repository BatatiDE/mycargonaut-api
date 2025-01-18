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
    val startingPoint: String,
    val destinationPoint: String,
    val date: LocalDateTime,
    val time: String,
    val price: Double,
    var availableSeats: Int,
    var freightSpace: Double,
    val isFreightRide: Boolean,
    val vehicle: String,
    val notes: String,

    @Enumerated(EnumType.STRING)
    val type: TripType,

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

enum class TripStatus {
    SCHEDULED,
    APPROACHING,
    ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELED,
    DELAYED
}

enum class TripType{
    OFFER,
    REQUEST
}

