package com.mycargonaut.backend.dto

data class UserResponseDTO(
    val id: Long,        // Read-only
    val email: String,   // Read-only
    val firstName: String?,   // Editable but fetched as part of the response
    val lastName: String?, // Editable but fetched as part of the response
    val phone: String?   // Editable but fetched as part of the response
)

data class UserUpdateDTO(
    val firstName: String?,   // Editable
    val lastName: String?,   // Editable
    val phone: String?   // Editable
)
