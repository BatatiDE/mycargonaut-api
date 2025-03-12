package com.mycargonaut.backend.entities

import jakarta.persistence.*

@Entity
@Table(name = "vehicles")
data class Vehicle(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: Long, // Verknüpfung zum Benutzer

    @Column(nullable = false)
    val type: String, // z. B. "Auto", "Transporter", "LKW"

    @Column(nullable = false)
    val brand: String,

    @Column(nullable = false)
    val model: String,

    @Column(nullable = false)
    val licensePlate: String,

    @Column(nullable = true)
    val description: String? = null,

    @Column(nullable = true)  // Statt nullable = false
    val capacity: Int? = null
)
