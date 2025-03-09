package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.entities.Rating
import com.mycargonaut.backend.services.RatingService
import graphql.GraphQLException
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RatingController(private val service: RatingService) {

    @MutationMapping
    fun addRating(
        @Argument voterId: Long,      // The user submitting the rating
        @Argument userId: Long,       // The user being rated
        @Argument tripId: Long,       // The trip associated with the rating
        @Argument ratingValue: Float  // The rating value
    ): Rating {
        return try {
            service.addRating(voterId, userId, tripId, ratingValue)
        } catch (ex: Exception) {
            // Throw the original exception without masking it
            throw GraphQLException("Error adding rating: ${ex.message}", ex)
        }
    }



    @QueryMapping
    fun getAllRatings(): List<Rating> {
        return service.getAllRatings()
    }

    @QueryMapping
    fun getAverageRatingByUser(@Argument userId: Long): Double {
        return service.getAverageRatingByUser(userId)
    }
}

