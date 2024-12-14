package com.mycargonaut.backend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    // Handle validation errors (e.g., for @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            fieldName to (error.defaultMessage ?: "Invalid value")
        }

        val response = mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Validation Failed",
            "message" to "Invalid input provided",
            "details" to errors
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    // Handle "not found" exceptions (e.g., missing resources)
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to "Resource Not Found",
            "message" to (ex.message ?: "Resource not found")
        )
        return ResponseEntity(response, HttpStatus.NOT_FOUND)
    }

    // Handle all other generic exceptions
    @ExceptionHandler(Exception::class)
    fun handleGenericErrors(ex: Exception): ResponseEntity<Map<String, Any>> {
        val response = mapOf(
            "timestamp" to LocalDateTime.now() as Any,
            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value() as Any,
            "error" to "Internal Server Error",
            "message" to (ex.message ?: "An unexpected error occurred"),
            "trace" to ex.stackTraceToString()
        )
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
