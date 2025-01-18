package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.dto.UserResponseDTO
import com.mycargonaut.backend.dto.UserUpdateDTO
import com.mycargonaut.backend.entities.User
import com.mycargonaut.backend.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository
) {

    /**
     * Fetch the logged-in user's profile.
     */
    @GetMapping("/me")
    fun getUserProfile(): ResponseEntity<UserResponseDTO> {
        val username = SecurityContextHolder.getContext().authentication.name

        val user = userRepository.findByEmail(username)
            ?: throw NoSuchElementException("User with email $username not found")

        return ResponseEntity.ok(toUserResponseDTO(user))
    }

    /**
     * Update the logged-in user's profile.
     */
    @PutMapping("/me")
    fun updateUserProfile(@RequestBody updatedUser: UserUpdateDTO): ResponseEntity<Map<String, String>> {
        val username = SecurityContextHolder.getContext().authentication.name

        // Log the incoming payload for debugging
        println("Received payload for update: $updatedUser")

        val user = userRepository.findByEmail(username)
            ?: throw NoSuchElementException("User with email $username not found")

        // Perform a partial update by copying only the updated fields
        val updatedEntity = user.copy(
            firstName = updatedUser.firstName ?: user.firstName, // If null, retain the original value
            lastName = updatedUser.lastName ?: user.lastName,
            phone = updatedUser.phone ?: user.phone
        )

        // Save the updated entity to the database
        userRepository.save(updatedEntity)

        // Return a JSON object as the response
        val response = mapOf("message" to "Profile updated successfully")
        return ResponseEntity.ok(response)
    }



    /**
     * Deactivate the logged-in user's account (soft delete).
     */
    @DeleteMapping("/me")
    fun deleteUser(): ResponseEntity<String> {
        val username = SecurityContextHolder.getContext().authentication.name

        val user = userRepository.findByEmail(username)
            ?: throw NoSuchElementException("User with email $username not found")

        userRepository.save(user.copy(isActive = false))

        return ResponseEntity.ok("User deactivated successfully")
    }

    /**
     * Mapping function: User -> UserResponseDTO
     */
    private fun toUserResponseDTO(user: User): UserResponseDTO {
        return UserResponseDTO(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            phone = user.phone
        )
    }

    /**
     * Mapping function: UserUpdateDTO -> User (Partial Update)
     */
    private fun updateUserFromDTO(user: User, userUpdateDTO: UserUpdateDTO): User {
        return user.copy(
            firstName = userUpdateDTO.firstName ?: user.firstName,
            lastName = userUpdateDTO.lastName ?: user.lastName,
            phone = userUpdateDTO.phone ?: user.phone
        )
    }
}
