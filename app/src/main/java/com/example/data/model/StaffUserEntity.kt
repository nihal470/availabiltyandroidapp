package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff_users")
data class StaffUserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val role: String, // Admin, Manager, Staff
    val phone: String = "+968 9123 4567",
    val avatarColorHex: String = "#DC2626"
)
