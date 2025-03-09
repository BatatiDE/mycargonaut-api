package com.mycargonaut.backend.entities

import jakarta.persistence.*
import com.mycargonaut.backend.entities.User
import com.mycargonaut.backend.entities.Trip

@Entity
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    val trip: Trip,

    val reviewText: String,

    val flagCount: Int = 0,

    @Enumerated(EnumType.STRING)
    val status: ReviewStatus = ReviewStatus.APPROVED
)

enum class ReviewStatus {
    APPROVED, FLAGGED
}
