package com.example.data.repository

import com.example.data.db.TravzDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.CustomerEntity
import com.example.data.model.LocationEntity
import com.example.data.model.MaintenanceEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.StaffRole
import com.example.data.model.StaffUserEntity
import com.example.data.model.VehicleAvailabilityItem
import com.example.data.model.VehicleEntity
import com.example.data.model.VehicleStatus
import com.example.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class TravzRepository(private val db: TravzDatabase) {

    val allVehicles: Flow<List<VehicleEntity>> = db.vehicleDao().getAllVehicles()
    val allBookings: Flow<List<BookingEntity>> = db.bookingDao().getAllBookings()
    val allMaintenance: Flow<List<MaintenanceEntity>> = db.maintenanceDao().getAllMaintenance()
    val allStaff: Flow<List<StaffUserEntity>> = db.staffDao().getAllStaff()
    val allLocations: Flow<List<LocationEntity>> = db.locationDao().getAllLocations()

    /**
     * Reactively computes vehicle availability for specific date range [pickupDate, dropoffDate]
     * optionally filtered by category.
     */
    fun getAvailabilityForDates(
        pickupDate: String,
        dropoffDate: String,
        categoryFilter: String? = null
    ): Flow<List<VehicleAvailabilityItem>> {
        return combine(
            allVehicles,
            allBookings,
            allMaintenance
        ) { vehicles, bookings, maintenanceList ->
            vehicles
                .filter { v ->
                    categoryFilter.isNullOrBlank() || categoryFilter.equals("All", ignoreCase = true) ||
                            v.category.equals(categoryFilter, ignoreCase = true)
                }
                .map { vehicle ->
                    // 1. Check permanent status overrides
                    if (vehicle.status.equals("Sold", ignoreCase = true)) {
                        return@map VehicleAvailabilityItem(
                            vehicle = vehicle,
                            calculatedStatus = VehicleStatus.SOLD
                        )
                    }
                    if (vehicle.status.equals("Idle", ignoreCase = true)) {
                        return@map VehicleAvailabilityItem(
                            vehicle = vehicle,
                            calculatedStatus = VehicleStatus.IDLE
                        )
                    }

                    // 2. Check overlapping bookings (excluding Cancelled)
                    val overlappingBooking = bookings.firstOrNull { booking ->
                        booking.vehicleId == vehicle.id &&
                                !booking.bookingStatus.equals("Cancelled", ignoreCase = true) &&
                                DateUtils.doDateRangesOverlap(
                                    startA = booking.pickupDate,
                                    endA = booking.dropoffDate,
                                    startB = pickupDate,
                                    endB = dropoffDate
                                )
                    }

                    if (overlappingBooking != null) {
                        return@map VehicleAvailabilityItem(
                            vehicle = vehicle,
                            calculatedStatus = VehicleStatus.BOOKED,
                            booking = overlappingBooking
                        )
                    }

                    // 3. Check overlapping maintenance (excluding Completed)
                    val overlappingMaintenance = maintenanceList.firstOrNull { maint ->
                        maint.vehicleId == vehicle.id &&
                                !maint.status.equals("Completed", ignoreCase = true) &&
                                DateUtils.doDateRangesOverlap(
                                    startA = maint.startDate,
                                    endA = maint.endDate,
                                    startB = pickupDate,
                                    endB = dropoffDate
                                )
                    }

                    if (overlappingMaintenance != null) {
                        return@map VehicleAvailabilityItem(
                            vehicle = vehicle,
                            calculatedStatus = VehicleStatus.MAINTENANCE,
                            maintenance = overlappingMaintenance
                        )
                    }

                    // 4. Default: Available
                    VehicleAvailabilityItem(
                        vehicle = vehicle,
                        calculatedStatus = VehicleStatus.AVAILABLE
                    )
                }
        }
    }

    /**
     * Reactively computes availability counts for today (or specified single date)
     */
    fun getDailySummary(targetDate: String = DateUtils.today()): Flow<DashboardSummary> {
        return getAvailabilityForDates(targetDate, targetDate).map { items ->
            val total = items.size
            val available = items.count { it.calculatedStatus == VehicleStatus.AVAILABLE }
            val booked = items.count { it.calculatedStatus == VehicleStatus.BOOKED }
            val maintenance = items.count { it.calculatedStatus == VehicleStatus.MAINTENANCE }
            DashboardSummary(
                totalVehicles = total,
                available = available,
                booked = booked,
                maintenance = maintenance,
                date = targetDate
            )
        }
    }

    suspend fun createBooking(
        customerName: String,
        customerPhone: String,
        customerEmail: String,
        vehicleId: Long,
        pickupDate: String,
        dropoffDate: String,
        pickupTime: String,
        dropoffTime: String,
        pickupLocation: String,
        dropoffLocation: String,
        dailyRate: Double,
        paymentStatus: String,
        bookingStatus: String,
        notes: String
    ): Long {
        val days = DateUtils.calculateDays(pickupDate, dropoffDate)
        val subtotal = dailyRate * days
        val vat = subtotal * 0.05
        val total = subtotal + vat

        val count = db.bookingDao().getBookingCount() + 1
        val bookingCode = "TRV-2026-%03d".format(count)

        // Save customer if not exists
        db.customerDao().insertCustomer(
            CustomerEntity(
                name = customerName,
                phone = customerPhone,
                email = customerEmail
            )
        )

        val newBooking = BookingEntity(
            bookingCode = bookingCode,
            customerName = customerName,
            customerPhone = customerPhone,
            customerEmail = customerEmail,
            vehicleId = vehicleId,
            pickupDate = pickupDate,
            dropoffDate = dropoffDate,
            pickupTime = pickupTime,
            dropoffTime = dropoffTime,
            pickupLocation = pickupLocation,
            dropoffLocation = dropoffLocation,
            dailyRate = dailyRate,
            rentalDays = days,
            subtotal = subtotal,
            vatAmount = vat,
            totalAmount = total,
            paymentStatus = paymentStatus,
            bookingStatus = bookingStatus,
            notes = notes
        )
        val bookingId = db.bookingDao().insertBooking(newBooking)

        // Add payment record if paid
        if (paymentStatus.equals("Paid", ignoreCase = true)) {
            db.paymentDao().insertPayment(
                PaymentEntity(
                    bookingId = bookingId,
                    amount = total,
                    paymentMethod = "Credit Card",
                    paymentDate = DateUtils.today(),
                    referenceNo = "TXN-${System.currentTimeMillis() % 1000000}"
                )
            )
        }

        return bookingId
    }

    suspend fun updateBooking(booking: BookingEntity) {
        db.bookingDao().updateBooking(booking)
    }

    suspend fun cancelBooking(bookingId: Long) {
        db.bookingDao().updateBookingStatus(bookingId, "Cancelled")
    }

    suspend fun updatePaymentStatus(bookingId: Long, paymentStatus: String) {
        db.bookingDao().updatePaymentStatus(bookingId, paymentStatus)
    }

    suspend fun insertVehicle(vehicle: VehicleEntity): Long {
        return db.vehicleDao().insertVehicle(vehicle)
    }

    suspend fun updateVehicle(vehicle: VehicleEntity) {
        db.vehicleDao().updateVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle: VehicleEntity) {
        db.vehicleDao().deleteVehicle(vehicle)
    }

    suspend fun updateVehicleStatus(vehicleId: Long, status: String) {
        db.vehicleDao().updateVehicleStatus(vehicleId, status)
    }

    suspend fun insertMaintenance(maintenance: MaintenanceEntity): Long {
        return db.maintenanceDao().insertMaintenance(maintenance)
    }

    suspend fun updateMaintenance(maintenance: MaintenanceEntity) {
        db.maintenanceDao().updateMaintenance(maintenance)
    }
}

data class DashboardSummary(
    val totalVehicles: Int = 0,
    val available: Int = 0,
    val booked: Int = 0,
    val maintenance: Int = 0,
    val date: String = ""
)
