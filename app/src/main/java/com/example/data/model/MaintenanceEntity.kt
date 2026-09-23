package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maintenance")
data class MaintenanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vehicleId: Long,
    val startDate: String, // YYYY-MM-DD
    val endDate: String, // YYYY-MM-DD
    val reason: String,
    val cost: Double = 0.0,
    val status: String = "InProgress", // InProgress, Scheduled, Completed
    val notes: String = ""
)
