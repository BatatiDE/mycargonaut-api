package com.mycargonaut.backend.dto
import java.time.LocalDate

data class UserResponseDTO(
   val id: Long,
       val email: String,
       val firstName: String?,
       val lastName: String?,
       val phone: String?,
       val picture: String?,
       val birthdate: LocalDate? = null,
       val additionalNote: String?,
       val rating: Double?,
       val numRides: Int?,
       val numPassengers: Int?,
       val weightCarried: Double?,
       val distanceTraveled: Double?,
       val languages: List<String>?,
       val isSmoker: Boolean?
)

data class UserUpdateDTO(
    val firstName: String?,
        val lastName: String?,
        val phone: String?,
        val birthdate: LocalDate?,
        val additionalNote: String?,
        val languages: Set<String>?,
        val isSmoker: Boolean?
)
