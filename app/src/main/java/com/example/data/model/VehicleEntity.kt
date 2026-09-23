package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val make: String,
    val model: String,
    val regNumber: String,
    val category: String, // Economy, Sedan, SUV, 4x4
    val modelYear: Int,
    val dailyRate: Double, // in OMR
    val status: String = "Available", // Available, Booked, Maintenance, Sold, Idle
    val imageDrawableName: String = "",
    val transmission: String = "Automatic",
    val seats: Int = 5,
    val fuelType: String = "Petrol",
    val mileageKm: Int = 15000
) {
    val fullName: String get() = "$make $model"
}
