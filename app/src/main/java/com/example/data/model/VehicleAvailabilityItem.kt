package com.example.data.model

data class VehicleAvailabilityItem(
    val vehicle: VehicleEntity,
    val calculatedStatus: VehicleStatus,
    val booking: BookingEntity? = null,
    val maintenance: MaintenanceEntity? = null,
    val isAvailable: Boolean = (calculatedStatus == VehicleStatus.AVAILABLE)
)
