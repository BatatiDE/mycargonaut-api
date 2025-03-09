package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.entities.Review
import com.mycargonaut.backend.services.ReviewService
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ReviewController(private val service: ReviewService) {

    @QueryMapping
    fun getAllReviews(): List<Review> = service.getAllReviews()

    @QueryMapping
    fun getReviewsByTrip(tripId: Long): List<Review> = service.getReviewsByTrip(tripId)

    @MutationMapping
    fun addReview(userId: Long, tripId: Long, reviewText: String): Review {
        return service.addReview(userId, tripId, reviewText)
    }
}
