package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.dto.UserDTO
import com.mycargonaut.backend.repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository
) {

    /**
     * Endpoint to fetch the logged-in user's profile.
     */
    @GetMapping("/me")
    fun getUserProfile(): ResponseEntity<UserDTO> {
        // Extract the logged-in user's email from the security context
        val username = SecurityContextHolder.getContext().authentication.name

        // Find the user in the database
        val user = userRepository.findByEmail(username)
            ?: return ResponseEntity.status(404).body(null)

        // Convert User entity to UserDTO
        val userDTO = UserDTO(
            id = user.id!!,
            email = user.email,
            name = user.name,
            birthdate = user.birthdate?.toString(),
            phone = user.phone,
            role = user.role,
            isVerified = user.isVerified,
            createdAt = user.createdAt.toString(),
            updatedAt = user.updatedAt.toString()
        )

        return ResponseEntity.ok(userDTO)
    }

    /**
     * Endpoint to update the logged-in user's profile.
     */
    @PutMapping("/me")
    fun updateUserProfile(@RequestBody updatedUser: UserDTO): ResponseEntity<Any> {
        return try {
            // Extract the logged-in user's email
            val username = SecurityContextHolder.getContext().authentication.name

            // Find the user in the database
            val user = userRepository.findByEmail(username)
                ?: return ResponseEntity.status(404).body(mapOf("error" to "User not found"))

            // Update the user's profile
            val updatedEntity = userRepository.save(
                user.copy(
                    name = updatedUser.name,
                    phone = updatedUser.phone,
                    birthdate = updatedUser.birthdate?.let { LocalDate.parse(it) }
                )
            )

            // Build a response with updated data
            val updatedUserDTO = UserDTO(
                id = updatedEntity.id!!,
                email = updatedEntity.email,
                name = updatedEntity.name,
                birthdate = updatedEntity.birthdate?.toString(),
                phone = updatedEntity.phone,
                role = updatedEntity.role,
                isVerified = updatedEntity.isVerified,
                createdAt = updatedEntity.createdAt.toString(),
                updatedAt = updatedEntity.updatedAt.toString()
            )

            ResponseEntity.ok(updatedUserDTO)

        } catch (e: Exception) {
            // Catch any unexpected error and return a detailed response
            val errorDetails = mapOf(
                "error" to "An error occurred while updating the profile",
                "message" to e.message,
                "timestamp" to LocalDateTime.now().toString()
            )
            ResponseEntity.status(500).body(errorDetails)
        }
    }


    @DeleteMapping("/me")
    fun deleteUser(): ResponseEntity<String> {
        val username = SecurityContextHolder.getContext().authentication.name

        val user = userRepository.findByEmail(username)
            ?: return ResponseEntity.status(404).body("User not found")

        // Perform a soft delete
        userRepository.save(user.copy(isActive = false))

        return ResponseEntity.ok("User deactivated successfully")
    }

}
