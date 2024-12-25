package com.mycargonaut.backend.services

import com.mycargonaut.backend.entities.Review
import com.mycargonaut.backend.entities.User
import com.mycargonaut.backend.entities.Trip
import com.mycargonaut.backend.repositories.ReviewRepository
import com.mycargonaut.backend.repositories.UserRepository
import com.mycargonaut.backend.repositories.TripRepository
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val repository: ReviewRepository,
    private val userRepository: UserRepository,
    private val tripRepository: TripRepository
) {

    fun addReview(userId: Long, tripId: Long, reviewText: String): Review {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with ID: $userId") }

        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found with ID: $tripId") }

        val review = Review(user = user, trip = trip, reviewText = reviewText)
        return repository.save(review)
    }

    fun getAllReviews(): List<Review> = repository.findAll()

    fun getReviewsByTrip(tripId: Long): List<Review> {
        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found with ID: $tripId") }
        return repository.findByTrip(trip)
    }
}
