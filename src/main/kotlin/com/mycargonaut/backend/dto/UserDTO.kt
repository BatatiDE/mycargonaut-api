package com.mycargonaut.backend.dto

data class UserResponseDTO(
    val id: Long,        // Read-only
    val email: String,   // Read-only
    val firstName: String,   // Read-only
    val lastName: String,   // Read-only
    val phone: String?   // Editable but fetched as part of the response
)

data class UserUpdateDTO(
    val firstName: String?,   // Editable
    val lastName: String?,   // Editable
    val phone: String?   // Editable
)
