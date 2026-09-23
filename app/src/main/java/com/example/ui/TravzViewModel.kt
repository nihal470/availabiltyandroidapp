package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.TravzDatabase
import com.example.data.model.BookingEntity
import com.example.data.model.LocationEntity
import com.example.data.model.MaintenanceEntity
import com.example.data.model.StaffRole
import com.example.data.model.StaffUserEntity
import com.example.data.model.VehicleAvailabilityItem
import com.example.data.model.VehicleCategory
import com.example.data.model.VehicleEntity
import com.example.data.model.VehicleStatus
import com.example.data.repository.DashboardSummary
import com.example.data.repository.TravzRepository
import com.example.util.DateUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String) {
    DASHBOARD("Dashboard"),
    SEARCH("Availability"),
    BOOKINGS("Bookings"),
    FLEET("Fleet"),
    CALENDAR("Calendar")
}

@OptIn(ExperimentalCoroutinesApi::class)
class TravzViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TravzRepository

    init {
        val database = TravzDatabase.getInstance(application)
        repository = TravzRepository(database)
    }

    // Active Navigation
    private val _currentTab = MutableStateFlow(AppNavTab.DASHBOARD)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Staff User & Role
    private val _currentStaff = MutableStateFlow(
        StaffUserEntity(
            id = 1,
            name = "Tariq Al-Riyami",
            email = "tariq.admin@travz.om",
            role = "Admin",
            avatarColorHex = "#DC2626"
        )
    )
    val currentStaff: StateFlow<StaffUserEntity> = _currentStaff.asStateFlow()

    val allStaff: StateFlow<List<StaffUserEntity>> = repository.allStaff
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search Parameters
    private val _pickupDate = MutableStateFlow("2026-09-24")
    val pickupDate: StateFlow<String> = _pickupDate.asStateFlow()

    private val _dropoffDate = MutableStateFlow("2026-09-28")
    val dropoffDate: StateFlow<String> = _dropoffDate.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Search Query trigger for explicit "Search" button
    private val _searchFilterTrigger = MutableStateFlow(
        Triple("2026-09-24", "2026-09-28", "All")
    )

    // Today's summary and availability
    val todaySummary: StateFlow<DashboardSummary> = repository.getDailySummary(DateUtils.today())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardSummary())

    val todayAvailability: StateFlow<List<VehicleAvailabilityItem>> =
        repository.getAvailabilityForDates(DateUtils.today(), DateUtils.today())
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search Results reactively matching the search filter
    val searchResults: StateFlow<List<VehicleAvailabilityItem>> = _searchFilterTrigger
        .flatMapLatest { (pickup, dropoff, cat) ->
            repository.getAvailabilityForDates(pickup, dropoff, cat)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Bookings
    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Vehicles (Fleet)
    val allVehicles: StateFlow<List<VehicleEntity>> = repository.allVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Locations
    val allLocations: StateFlow<List<LocationEntity>> = repository.allLocations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calendar Selected Date & Availability
    private val _calendarDate = MutableStateFlow(DateUtils.today())
    val calendarDate: StateFlow<String> = _calendarDate.asStateFlow()

    val calendarAvailability: StateFlow<List<VehicleAvailabilityItem>> = _calendarDate
        .flatMapLatest { date ->
            repository.getAvailabilityForDates(date, date)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialog & Action States
    var bookingTargetVehicle = MutableStateFlow<VehicleAvailabilityItem?>(null)
    var selectedBookingForDetails = MutableStateFlow<BookingEntity?>(null)
    var bookingToEdit = MutableStateFlow<BookingEntity?>(null)
    var vehicleToEdit = MutableStateFlow<VehicleEntity?>(null)
    var showAddVehicleDialog = MutableStateFlow(false)
    var showStaffSwitcher = MutableStateFlow(false)

    // Snackbar / Feedback message
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    fun selectTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun switchStaff(staff: StaffUserEntity) {
        _currentStaff.value = staff
        showStaffSwitcher.value = false
        showMessage("Logged in as ${staff.name} (${staff.role})")
    }

    fun setPickupDate(date: String) {
        _pickupDate.value = date
        // If pickup > dropoff, adjust dropoff
        if (date > _dropoffDate.value) {
            _dropoffDate.value = DateUtils.addDays(date, 3)
        }
    }

    fun setDropoffDate(date: String) {
        _dropoffDate.value = date
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun executeSearch() {
        _searchFilterTrigger.value = Triple(
            _pickupDate.value,
            _dropoffDate.value,
            _selectedCategory.value
        )
        _currentTab.value = AppNavTab.SEARCH
    }

    fun setCalendarDate(date: String) {
        _calendarDate.value = date
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    private fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    // Booking Operations
    fun submitBooking(
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
    ) {
        viewModelScope.launch {
            try {
                val bookingId = repository.createBooking(
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
                    paymentStatus = paymentStatus,
                    bookingStatus = bookingStatus,
                    notes = notes
                )
                bookingTargetVehicle.value = null
                showMessage("Booking created successfully (ID: $bookingId)")
            } catch (e: Exception) {
                showMessage("Failed to create booking: ${e.localizedMessage}")
            }
        }
    }

    fun updateBooking(booking: BookingEntity) {
        viewModelScope.launch {
            try {
                repository.updateBooking(booking)
                bookingToEdit.value = null
                showMessage("Booking ${booking.bookingCode} updated")
            } catch (e: Exception) {
                showMessage("Error updating booking: ${e.localizedMessage}")
            }
        }
    }

    fun cancelBooking(bookingId: Long, bookingCode: String) {
        viewModelScope.launch {
            try {
                repository.cancelBooking(bookingId)
                selectedBookingForDetails.value = null
                showMessage("Booking $bookingCode cancelled")
            } catch (e: Exception) {
                showMessage("Error cancelling booking: ${e.localizedMessage}")
            }
        }
    }

    fun updatePaymentStatus(bookingId: Long, newStatus: String) {
        viewModelScope.launch {
            try {
                repository.updatePaymentStatus(bookingId, newStatus)
                // Refresh detail modal if open
                selectedBookingForDetails.value?.let { current ->
                    if (current.id == bookingId) {
                        selectedBookingForDetails.value = current.copy(paymentStatus = newStatus)
                    }
                }
                showMessage("Payment status changed to $newStatus")
            } catch (e: Exception) {
                showMessage("Error updating payment: ${e.localizedMessage}")
            }
        }
    }

    // Fleet Operations
    fun saveVehicle(vehicle: VehicleEntity, isNew: Boolean) {
        viewModelScope.launch {
            try {
                if (isNew) {
                    repository.insertVehicle(vehicle)
                    showMessage("New vehicle ${vehicle.fullName} added to fleet")
                } else {
                    repository.updateVehicle(vehicle)
                    showMessage("Vehicle ${vehicle.fullName} updated")
                }
                vehicleToEdit.value = null
                showAddVehicleDialog.value = false
            } catch (e: Exception) {
                showMessage("Error saving vehicle: ${e.localizedMessage}")
            }
        }
    }

    fun updateVehicleStatus(vehicleId: Long, newStatus: String) {
        viewModelScope.launch {
            try {
                repository.updateVehicleStatus(vehicleId, newStatus)
                showMessage("Vehicle status set to $newStatus")
            } catch (e: Exception) {
                showMessage("Error updating vehicle status: ${e.localizedMessage}")
            }
        }
    }

    fun deleteVehicle(vehicle: VehicleEntity) {
        viewModelScope.launch {
            try {
                repository.deleteVehicle(vehicle)
                showMessage("Vehicle ${vehicle.fullName} removed from fleet")
            } catch (e: Exception) {
                showMessage("Error removing vehicle: ${e.localizedMessage}")
            }
        }
    }
}
