package com.mycargonaut.backend.services

import com.mycargonaut.backend.dto.VehicleDto
import com.mycargonaut.backend.entities.Vehicle  // KORRIGIERT
import com.mycargonaut.backend.repositories.VehicleRepository // KORRIGIERT
import org.springframework.stereotype.Service

@Service
class VehicleService(private val vehicleRepository: VehicleRepository) {

    fun getAllVehicles(): List<Vehicle> = vehicleRepository.findAll()

    fun getVehicleById(id: Long): Vehicle? = vehicleRepository.findById(id).orElse(null)

    fun createVehicle(vehicleDto: VehicleDto): Vehicle {
        val vehicle = Vehicle(
            userId = vehicleDto.userId,
            type = vehicleDto.type,
            brand = vehicleDto.brand,
            model = vehicleDto.model,
            licensePlate = vehicleDto.licensePlate,
            description = null
        )
        return vehicleRepository.save(vehicle)
    }

    fun updateVehicle(id: Long, vehicleDto: VehicleDto): Vehicle? {
        val existingVehicle = vehicleRepository.findById(id).orElse(null) ?: return null
        val updatedVehicle = existingVehicle.copy(
            type = vehicleDto.type,
            brand = vehicleDto.brand,
            model = vehicleDto.model,
            licensePlate = vehicleDto.licensePlate
        )
        return vehicleRepository.save(updatedVehicle)
    }

    fun deleteVehicle(id: Long) {
        vehicleRepository.deleteById(id)
    }
}