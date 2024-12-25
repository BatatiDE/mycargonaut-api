package com.mycargonaut.backend.entities

import jakarta.persistence.*

@Entity
data class Rating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    val trip: Trip,

    val ratingValue: Float
)
