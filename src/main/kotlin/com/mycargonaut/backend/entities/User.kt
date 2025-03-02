package com.mycargonaut.backend.entities

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val email: String,
    val password: String,
    val name: String? = null,
    val birthdate: LocalDate? = null,
    val phone: String? = null,
    val role: String = "USER",
    val isVerified: Boolean = false,
    val isActive: Boolean = true,  // HINZUGEFÜGT
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
