package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.entities.Trip
import com.mycargonaut.backend.entities.User
import com.mycargonaut.backend.repositories.TripRepository
import com.mycargonaut.backend.repositories.UserRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class TripController(
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository
) {

    @MutationMapping
    fun addTrip(@Argument input: AddTripInput): Trip {
        val driver: User = userRepository.findById(input.driverId)
            .orElseThrow { IllegalArgumentException("Driver not found") }

        val trip = Trip(
            driver = driver,
            startPoint = input.startPoint,
            destinationPoint = input.destinationPoint,
            date = input.date,
            time = input.time,
            availableSpace = input.availableSpace
        )

        return tripRepository.save(trip)
    }

    @QueryMapping
    fun getTrips(): List<Trip> = tripRepository.findAll()
}

data class AddTripInput(
    val driverId: Long,
    val startPoint: String,
    val destinationPoint: String,
    val date: String,
    val time: String,
    val availableSpace: Int
)
