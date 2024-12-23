package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.entities.Booking
import com.mycargonaut.backend.entities.Trip
import com.mycargonaut.backend.repositories.BookingRepository
import com.mycargonaut.backend.repositories.TripRepository
import com.mycargonaut.backend.repositories.UserRepository
import com.mycargonaut.backend.entities.User
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.security.core.context.SecurityContextHolder

@Controller
class TripController(
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository
) {

    // Mutation: Add a new trip
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
            totalCapacity = input.availableSpace,
            availableSpace = input.availableSpace

        )

        return tripRepository.save(trip)
    }

    // Query: Get all trips
  //  fun getTrips(): List<Trip> = tripRepository.findAll()

    @QueryMapping
    fun getTrips(): List<Map<String, Any>> {
        val trips = tripRepository.findAll()
        return trips.map { trip ->
            mapOf(
                "id" to (trip.id ?: 0L), // Ensure non-null id
                "driverId" to (trip.driver.id ?: 0L), // Ensure non-null driverId
                "startPoint" to trip.startPoint,
                "destinationPoint" to trip.destinationPoint,
                "date" to trip.date,
                "time" to trip.time,
                "availableSpace" to trip.availableSpace,
                "total_capacity" to trip.totalCapacity, // Ensure this field is included
                "status" to trip.status,// Include status
                "bookedUsers" to bookingRepository.findByTrip(trip).map { booking ->
                    mapOf(
                        "id" to booking.id,
                        "userId" to booking.user.id,
                        "status" to booking.status,
                        "createdAt" to booking.createdAt
                    )
                }

            )
        }
    }




    // Mutation: Book a trip
    @MutationMapping
    fun bookTrip(@Argument tripId: Long): BookingResponse {
        val username = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("User not found")

        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found") }

        if (trip.availableSpace <= 0) {
            return BookingResponse(
                success = false,
                message = "No available space for this trip",
                booking = null
            )
        }

        // Reduce available space and save booking
        trip.availableSpace -= 1
        val booking = Booking(user = user, trip = trip)
        bookingRepository.save(booking)
        tripRepository.save(trip)

        return BookingResponse(
            success = true,
            message = "Trip booked successfully",
            booking = booking
        )
    }

    @MutationMapping
    fun confirmBooking(@Argument bookingId: Long): BookingResponse {
        val username = SecurityContextHolder.getContext().authentication?.name
            ?: throw IllegalArgumentException("User is not authenticated")

        val driver = userRepository.findByEmail(username)
            ?: throw IllegalArgumentException("Driver not found. Username: $username")


        println("Driver found: ${driver.email} with ID ${driver.id}")

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { IllegalArgumentException("Booking not found for ID: $bookingId") }

        if (booking.trip.driver.id != driver.id) {
            throw IllegalArgumentException("You are not authorized to confirm this booking")
        }

        booking.status = "Confirmed"
        bookingRepository.save(booking)

        return BookingResponse(
            success = true,
            message = "Booking confirmed successfully",
            booking = booking
        )
    }


    @MutationMapping
    fun updateUserRole(@Argument userId: Long, @Argument role: String): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (role != "USER" && role != "DRIVER") {
            throw IllegalArgumentException("Invalid role provided: $role")
        }

        user.role = role
        return userRepository.save(user)
    }



}

// Input type for adding a new trip
data class AddTripInput(
    val driverId: Long,
    val startPoint: String,
    val destinationPoint: String,
    val date: String,
    val time: String,
    val availableSpace: Int
)

// Response for booking a trip
data class BookingResponse(
    val success: Boolean,
    val message: String,
    val booking: Booking?
)

