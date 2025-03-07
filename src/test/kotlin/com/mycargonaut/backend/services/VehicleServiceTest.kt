package com.mycargonaut.backend.services

import com.mycargonaut.backend.entities.Vehicle
import com.mycargonaut.backend.dto.VehicleDto
import com.mycargonaut.backend.repositories.VehicleRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class VehicleServiceTest {

    private lateinit var vehicleService: VehicleService
    private val vehicleRepository: VehicleRepository = mock(VehicleRepository::class.java)

    @BeforeEach
    fun setup() {
        vehicleService = VehicleService(vehicleRepository)
    }

    @Test
    fun `should return all vehicles`() {
        val vehicles = listOf(
            Vehicle(id = 1, type = "Auto", brand = "BMW", model = "X5", licensePlate = "ABC-123", userId = 1),
            Vehicle(id = 2, type = "Transporter", brand = "Mercedes", model = "Sprinter", licensePlate = "XYZ-789", userId = 2)
        )
        `when`(vehicleRepository.findAll()).thenReturn(vehicles)

        val result = vehicleService.getAllVehicles()

        assertEquals(2, result.size)
        assertEquals("BMW", result[0].brand)
    }

    @Test
    fun `should find vehicle by id`() {
        val vehicle = Vehicle(id = 1, type = "Auto", brand = "BMW", model = "X5", licensePlate = "ABC-123", userId = 1)
        `when`(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle))

        val result = vehicleService.getVehicleById(1)

        assertNotNull(result)
        assertEquals("BMW", result?.brand)
    }

    @Test
    fun `should create a new vehicle`() {
        val vehicleDto = VehicleDto(id = null, type = "Auto", brand = "BMW", model = "X5", licensePlate = "ABC-123", userId = 1)
        val vehicle = Vehicle(id = 1, type = "Auto", brand = "BMW", model = "X5", licensePlate = "ABC-123", userId = 1)

        `when`(vehicleRepository.save(any(Vehicle::class.java))).thenReturn(vehicle)

        val result = vehicleService.createVehicle(vehicleDto)

        assertNotNull(result)
        assertEquals("BMW", result.brand)
    }

    @Test
    fun `should delete a vehicle`() {
        val id = 1L
        doNothing().`when`(vehicleRepository).deleteById(id)

        vehicleService.deleteVehicle(id)

        verify(vehicleRepository, times(1)).deleteById(id)
    }
}
