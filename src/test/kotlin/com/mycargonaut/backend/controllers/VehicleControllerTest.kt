package com.mycargonaut.backend.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.mycargonaut.backend.dto.VehicleDto
import com.mycargonaut.backend.entities.Vehicle
import com.mycargonaut.backend.services.VehicleService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@ExtendWith(MockitoExtension::class)  // ✅ Keine Spring Beans mehr nötig
class VehicleControllerTest {

    private lateinit var mockMvc: MockMvc
    private lateinit var vehicleService: VehicleService
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        vehicleService = mock(VehicleService::class.java)  // 🔥 Direktes Mockito-Mocking
        val vehicleController = VehicleController(vehicleService)  // Manuelle Instanzierung
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build()
    }

    @Test
    fun `should return all vehicles`() {
        val vehicle = Vehicle(1L, 1L, "Auto", "BMW", "3er", "AB-1234", null)
        `when`(vehicleService.getAllVehicles()).thenReturn(listOf(vehicle))

        mockMvc.perform(get("/api/vehicles"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].brand").value("BMW"))
    }

    @Test
    fun `should return vehicle by ID`() {
        val vehicle = Vehicle(1L, 1L, "Auto", "BMW", "3er", "AB-1234", null)
        `when`(vehicleService.getVehicleById(1L)).thenReturn(vehicle)

        mockMvc.perform(get("/api/vehicles/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.brand").value("BMW"))
    }

    @Test
    fun `should create a new vehicle`() {
        val vehicleDto = VehicleDto(null, "Auto", "BMW", "3er", "AB-1234", 1L)
        val vehicle = Vehicle(1L, 1L, "Auto", "BMW", "3er", "AB-1234", null)

        `when`(vehicleService.createVehicle(vehicleDto)).thenReturn(vehicle)

        mockMvc.perform(
            post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    fun `should update a vehicle`() {
        val updatedVehicle = Vehicle(1L, 1L, "Auto", "Audi", "A4", "XY-5678", null)
        val updatedVehicleDto = VehicleDto(1L, "Auto", "Audi", "A4", "XY-5678", 1L)

        `when`(vehicleService.updateVehicle(1L, updatedVehicleDto)).thenReturn(updatedVehicle)

        mockMvc.perform(
            put("/api/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedVehicleDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.brand").value("Audi"))
    }

    @Test
    fun `should delete a vehicle`() {
        doNothing().`when`(vehicleService).deleteVehicle(1L)

        mockMvc.perform(delete("/api/vehicles/1"))
            .andExpect(status().isNoContent)
    }
}
