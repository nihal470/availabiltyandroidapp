package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingCode: String,
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val vehicleId: Long,
    val pickupDate: String, // YYYY-MM-DD
    val dropoffDate: String, // YYYY-MM-DD
    val pickupTime: String = "10:00 AM",
    val dropoffTime: String = "10:00 AM",
    val pickupLocation: String = "Muscat International Airport",
    val dropoffLocation: String = "Muscat International Airport",
    val dailyRate: Double,
    val rentalDays: Int,
    val subtotal: Double,
    val vatAmount: Double, // 5%
    val totalAmount: Double,
    val paymentStatus: String = "Unpaid", // Paid, Partial, Unpaid
    val bookingStatus: String = "Confirmed", // Confirmed, Active, Completed, Cancelled, Pending
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
