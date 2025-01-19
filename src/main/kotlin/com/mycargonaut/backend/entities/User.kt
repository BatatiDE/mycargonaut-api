package com.mycargonaut.backend.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
        val email: String,
        val password: String,
        var role: String = "USER",
        var firstName: String? = null,
        var lastName: String? = null,
        var phone: String? = null,
        var picture: String? = null,
        var birthdate: LocalDate? = null,
        var additionalNote: String? = null,
        var rating: Double? = null,
        var numRides: Int? = null,
        var numPassengers: Int? = null,
        var weightCarried: Double? = null,
        var distanceTraveled: Double? = null,
        @ElementCollection
        @CollectionTable(name = "user_languages", joinColumns = [JoinColumn(name = "user_id")])
        @Column(name = "language")
        var languages: MutableSet<String> = mutableSetOf(),
        var isSmoker: Boolean? = null,

    // Soft delete field
    var isActive: Boolean = true,
    var isVerified: Boolean = false,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "driver", cascade = [CascadeType.ALL], orphanRemoval = true)
    val trips: MutableList<Trip> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookings: MutableList<Booking> = mutableListOf()
) {
    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
