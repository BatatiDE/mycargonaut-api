package com.mycargonaut.backend.dto

data class LoginResponseDTO(
    val token: String,
    val user: UserBasicInfoDTO
)

data class UserBasicInfoDTO(
    val id: Long,
    val email: String,
    val firstName: String?,
    val lastName: String?
)

