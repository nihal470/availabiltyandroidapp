package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookingId: Long,
    val amount: Double,
    val paymentMethod: String = "Credit Card", // Credit Card, Cash, Bank Transfer, Debit Card
    val paymentDate: String,
    val referenceNo: String,
    val status: String = "Completed"
)
