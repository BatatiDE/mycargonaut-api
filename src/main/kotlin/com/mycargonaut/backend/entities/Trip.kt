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
    val availableSpace: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
