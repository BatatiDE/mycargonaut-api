package com.mycargonaut.backend.dto

data class VehicleDto(
    val id: Long?,
    val type: String,
    val brand: String,
    val model: String,
    val licensePlate: String,
    val userId: Long
)
