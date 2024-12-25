package com.mycargonaut.backend.services

import com.mycargonaut.backend.entities.Rating
import com.mycargonaut.backend.repositories.RatingRepository
import com.mycargonaut.backend.repositories.UserRepository
import com.mycargonaut.backend.repositories.TripRepository
import org.springframework.stereotype.Service

@Service
class RatingService(
    private val ratingRepository: RatingRepository,
    private val userRepository: UserRepository,
    private val tripRepository: TripRepository
) {
    fun addRating(userId: Long, tripId: Long, ratingValue: Float): Rating {
        val user = userRepository.findById(userId).orElseThrow {
            IllegalArgumentException("User not found with ID: $userId")
        }
        val trip = tripRepository.findById(tripId).orElseThrow {
            IllegalArgumentException("Trip not found with ID: $tripId")
        }
        val existingRating = ratingRepository.findByUserIdAndTripId(userId, tripId)
        if (existingRating != null) {
            throw IllegalArgumentException("You have already rated this trip.")
        }

        val newRating = Rating(
            user = user,
            trip = trip,
            ratingValue = ratingValue
        )
        return ratingRepository.save(newRating)
    }

    fun getAllRatings(): List<Rating> {
        return ratingRepository.findAll()
    }

    fun getAverageRatingByUser(userId: Long): Double {
        val ratings = ratingRepository.findByUserId(userId)
        if (ratings.isEmpty()) {
            throw IllegalArgumentException("No ratings found for the user with ID $userId.")
        }
        return ratings.map { it.ratingValue }.average()
    }
}
