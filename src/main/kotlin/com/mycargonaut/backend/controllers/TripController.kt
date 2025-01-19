package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.entities.Booking
import com.mycargonaut.backend.entities.Trip
import com.mycargonaut.backend.entities.TripStatus
import com.mycargonaut.backend.entities.TripType
import com.mycargonaut.backend.repositories.BookingRepository
import com.mycargonaut.backend.repositories.TripRepository
import com.mycargonaut.backend.repositories.UserRepository
import com.mycargonaut.backend.entities.User
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Controller
class TripController(
    private val tripRepository: TripRepository,
    private val userRepository: UserRepository,
    private val bookingRepository: BookingRepository
) {

    @MutationMapping
    fun addTrip(@Argument input: AddTripInput): Trip {
        val driver: User = userRepository.findById(input.driverId)
            .orElseThrow { IllegalArgumentException("Driver not found") }

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val date = LocalDate.parse(input.date, dateFormatter)
        val time = LocalTime.parse(input.time, timeFormatter)
        val dateTime = LocalDateTime.of(date, time)

        val trip = Trip(
                driver = driver,
                startingPoint = input.startingPoint,
                destinationPoint = input.destinationPoint,
                date = dateTime,
                time = input.time,
                price = input.price,
                availableSeats = input.availableSeats,
                freightSpace = input.freightSpace,
                isFreightRide = input.isFreightRide,
                vehicle = input.vehicle ?: "",  // Provide a default empty string if vehicle is null
                notes = input.notes ?: "",      // Provide a default empty string if notes is null
                type = input.type
            )

        return tripRepository.save(trip)
    }


    @QueryMapping
    fun getTrips(): List<Map<String, Any>> {
        val trips = tripRepository.findAll()
        return trips.map { trip ->
            mapOf(
                "id" to (trip.id ?: 0L),
                "driverId" to (trip.driver.id ?: 0L),
                "startingPoint" to trip.startingPoint,
                "destinationPoint" to trip.destinationPoint,
                "date" to trip.date,
                "time" to trip.time,
                "price" to trip.price,
                "availableSeats" to trip.availableSeats,
                "freightSpace" to trip.freightSpace,
                "isFreightRide" to trip.isFreightRide,
                "vehicle" to trip.vehicle,
                "notes" to trip.notes,
                "status" to trip.status,
                "type" to trip.type,
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

    @QueryMapping
    fun getUpcomingTrips(): List<Map<String, Any>> {
        val now = LocalDateTime.now()
        val trips = tripRepository.findByDateAfterAndStatusIn(now, listOf(TripStatus.SCHEDULED, TripStatus.APPROACHING))
        return trips.map { trip ->
            mapOf(
                "id" to (trip.id ?: 0L),
                "driverId" to (trip.driver.id ?: 0L),
                "startingPoint" to trip.startingPoint,
                "destinationPoint" to trip.destinationPoint,
                "date" to trip.date,
                "time" to trip.time,
                "price" to trip.price,
                "availableSeats" to trip.availableSeats,
                "freightSpace" to trip.freightSpace,
                "isFreightRide" to trip.isFreightRide,
                "vehicle" to trip.vehicle,
                "notes" to trip.notes,
                "status" to trip.status,
                "type" to trip.type
            )
        }
    }

    @MutationMapping
    fun bookTrip(@Argument tripId: Long, @Argument seats: Int = 1, @Argument freightSpace: Double = 0.0): BookingResponse {
         val username = SecurityContextHolder.getContext().authentication.name
                val user = userRepository.findByEmail(username)
                    ?: throw IllegalArgumentException("User not found")

                val trip = tripRepository.findById(tripId)
                    .orElseThrow { IllegalArgumentException("Trip not found") }

                if (trip.availableSeats <= 0) {
                    return BookingResponse(
                        success = false,
                        message = "No available space for this trip",
                        booking = null
                    )
                }

                // Reduce available space and save booking
                trip.availableSeats -= 1
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

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { IllegalArgumentException("Booking not found for ID: $bookingId") }

        if (booking.trip.driver.id != driver.id) {
            throw IllegalArgumentException("You are not authorized to confirm this booking")
        }

        val updatedBooking = booking.copy(status = "Confirmed")
        bookingRepository.save(updatedBooking)

        return BookingResponse(
            success = true,
            message = "Booking confirmed successfully",
            booking = updatedBooking
        )
    }

    @MutationMapping
    fun updateTripStatus(@Argument tripId: Long, @Argument status: TripStatus): Trip {
        val trip = tripRepository.findById(tripId)
            .orElseThrow { IllegalArgumentException("Trip not found") }

        trip.status = status
        return tripRepository.save(trip)
    }

    @MutationMapping
    fun updateUserRole(@Argument userId: Long, @Argument role: String): User {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (role != "USER" && role != "DRIVER") {
            throw IllegalArgumentException("Invalid role provided: $role")
        }

        val updatedUser = user.copy(role = role)
        return userRepository.save(updatedUser)
    }
}

data class AddTripInput(
    val driverId: Long,
    val startingPoint: String,
    val destinationPoint: String,
    val date: String,
    val time: String,
    val price: Double,
    val availableSeats: Int,
    val freightSpace: Double,
    val isFreightRide: Boolean,
    val vehicle: String,
    val notes: String,
    val type: TripType
)

data class BookingResponse(
    val success: Boolean,
    val message: String,
    val booking: Booking?
)