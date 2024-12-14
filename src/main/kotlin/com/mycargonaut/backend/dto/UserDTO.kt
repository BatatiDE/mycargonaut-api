package com.mycargonaut.backend.dto

data class UserResponseDTO(
    val id: Long,        // Read-only
    val email: String,   // Read-only
    val name: String?,   // Editable but fetched as part of the response
    val phone: String?   // Editable but fetched as part of the response
)

data class UserUpdateDTO(
    val name: String?,   // Editable
    val phone: String?   // Editable
)
