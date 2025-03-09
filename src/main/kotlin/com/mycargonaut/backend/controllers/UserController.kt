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

    @GetMapping("/me")
    fun getUserProfile(): ResponseEntity<UserResponseDTO> {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(username)
            ?: throw NoSuchElementException("User with email $username not found")
        return ResponseEntity.ok(user.toUserResponseDTO())
    }

    @PutMapping("/me")
    fun updateUserProfile(@Valid @RequestBody updatedUser: UserUpdateDTO): ResponseEntity<UserResponseDTO> {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(username)
            ?: throw NoSuchElementException("User with email $username not found")

        val updatedEntity = user.copy(
            firstName = updatedUser.firstName ?: user.firstName,
            lastName = updatedUser.lastName ?: user.lastName,
            phone = updatedUser.phone ?: user.phone,
            birthdate = updatedUser.birthdate ?: user.birthdate,
            additionalNote = updatedUser.additionalNote ?: user.additionalNote,
            isSmoker = updatedUser.isSmoker ?: user.isSmoker
        ).apply {
            updatedUser.languages?.let { languages = it.toMutableSet() }
        }

        val savedUser = userRepository.save(updatedEntity)
        return ResponseEntity.ok(savedUser.toUserResponseDTO())
    }

    @DeleteMapping("/me")
    fun deleteUser(): ResponseEntity<String> {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(username)
            ?: throw NoSuchElementException("User with email $username not found")
        userRepository.save(user.copy(isActive = false))
        return ResponseEntity.ok("User deactivated successfully")
    }

    private fun User.toUserResponseDTO(): UserResponseDTO {
        return UserResponseDTO(
            id = id!!,
            email = email,
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            picture = picture,
            birthdate = birthdate,
            additionalNote = additionalNote,
            rating = rating,
            numRides = numRides,
            numPassengers = numPassengers,
            weightCarried = weightCarried,
            distanceTraveled = distanceTraveled,
            languages = languages.toList(),
            isSmoker = isSmoker
        )
    }
}

