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
    val name: String? = null,
    val birthdate: LocalDate? = null,
    val phone: String? = null,
    var role: String = "USER",
    val isVerified: Boolean = false,

    // Soft delete field
    var isActive: Boolean = true,

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
