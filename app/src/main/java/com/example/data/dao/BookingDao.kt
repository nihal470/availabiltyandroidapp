package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getBookingById(id: Long): BookingEntity?

    @Query("SELECT * FROM bookings WHERE vehicleId = :vehicleId AND bookingStatus != 'Cancelled'")
    fun getBookingsForVehicle(vehicleId: Long): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE bookingStatus != 'Cancelled'")
    suspend fun getAllActiveBookings(): List<BookingEntity>

    @Query("SELECT COUNT(*) FROM bookings")
    suspend fun getBookingCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET paymentStatus = :paymentStatus WHERE id = :id")
    suspend fun updatePaymentStatus(id: Long, paymentStatus: String)

    @Query("UPDATE bookings SET bookingStatus = :bookingStatus WHERE id = :id")
    suspend fun updateBookingStatus(id: Long, bookingStatus: String)
}
