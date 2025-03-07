package com.mycargonaut.backend.controllers

import com.mycargonaut.backend.dto.VehicleDto
import com.mycargonaut.backend.entities.Vehicle  // KORRIGIERT
import com.mycargonaut.backend.services.VehicleService // KORRIGIERT
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vehicles")
class VehicleController(private val vehicleService: VehicleService) {

    @GetMapping
    fun getAllVehicles(): ResponseEntity<List<Vehicle>> =
        ResponseEntity.ok(vehicleService.getAllVehicles())

    @GetMapping("/{id}")
    fun getVehicleById(@PathVariable id: Long): ResponseEntity<Vehicle> {
        val vehicle = vehicleService.getVehicleById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(vehicle)
    }

    @PostMapping
    fun createVehicle(@RequestBody vehicleDto: VehicleDto): ResponseEntity<Vehicle> =
        ResponseEntity.ok(vehicleService.createVehicle(vehicleDto))

    @PutMapping("/{id}")
    fun updateVehicle(@PathVariable id: Long, @RequestBody vehicleDto: VehicleDto): ResponseEntity<Vehicle> {
        val updatedVehicle = vehicleService.updateVehicle(id, vehicleDto) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedVehicle)
    }

    @DeleteMapping("/{id}")
    fun deleteVehicle(@PathVariable id: Long): ResponseEntity<Void> {
        vehicleService.deleteVehicle(id)
        return ResponseEntity.noContent().build()
    }
}
