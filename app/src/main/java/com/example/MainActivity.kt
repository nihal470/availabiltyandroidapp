package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StaffRole
import com.example.ui.AppNavTab
import com.example.ui.TravzViewModel
import com.example.ui.components.TravzTopHeader
import com.example.ui.dialogs.AddEditVehicleDialog
import com.example.ui.dialogs.BookingDetailsDialog
import com.example.ui.dialogs.BookingDialog
import com.example.ui.dialogs.StaffSwitcherDialog
import com.example.ui.screens.AvailabilitySearchScreen
import com.example.ui.screens.BookingsManagementScreen
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FleetManagementScreen
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTheme
import com.example.ui.theme.TravzWhite

class MainActivity : ComponentActivity() {

    private val viewModel: TravzViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravzTheme {
                TravzApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TravzApp(viewModel: TravzViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentStaff by viewModel.currentStaff.collectAsStateWithLifecycle()
    val allStaff by viewModel.allStaff.collectAsStateWithLifecycle()

    val todaySummary by viewModel.todaySummary.collectAsStateWithLifecycle()
    val todayAvailability by viewModel.todayAvailability.collectAsStateWithLifecycle()

    val pickupDate by viewModel.pickupDate.collectAsStateWithLifecycle()
    val dropoffDate by viewModel.dropoffDate.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    val allBookings by viewModel.allBookings.collectAsStateWithLifecycle()
    val allVehicles by viewModel.allVehicles.collectAsStateWithLifecycle()
    val allLocations by viewModel.allLocations.collectAsStateWithLifecycle()

    val calendarDate by viewModel.calendarDate.collectAsStateWithLifecycle()
    val calendarAvailability by viewModel.calendarAvailability.collectAsStateWithLifecycle()

    val bookingTargetVehicle by viewModel.bookingTargetVehicle.collectAsStateWithLifecycle()
    val selectedBookingForDetails by viewModel.selectedBookingForDetails.collectAsStateWithLifecycle()
    val bookingToEdit by viewModel.bookingToEdit.collectAsStateWithLifecycle()
    val vehicleToEdit by viewModel.vehicleToEdit.collectAsStateWithLifecycle()
    val showAddVehicleDialog by viewModel.showAddVehicleDialog.collectAsStateWithLifecycle()
    val showStaffSwitcher by viewModel.showStaffSwitcher.collectAsStateWithLifecycle()

    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    val staffRoleEnum = when (currentStaff.role) {
        "Admin" -> StaffRole.ADMIN
        "Manager" -> StaffRole.MANAGER
        else -> StaffRole.STAFF
    }
    val canManageFleet = staffRoleEnum.canManageFleet()
    val canCancelOrEdit = staffRoleEnum.canDeleteOrCancel()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            TravzTopHeader(
                currentStaff = currentStaff,
                onStaffClick = { viewModel.showStaffSwitcher.value = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = TravzBlack,
                contentColor = TravzWhite,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBarItem(
                    selected = currentTab == AppNavTab.DASHBOARD,
                    onClick = { viewModel.selectTab(AppNavTab.DASHBOARD) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TravzWhite,
                        selectedTextColor = TravzRedPrimary,
                        unselectedIconColor = Color(0xFFA1A1AA),
                        unselectedTextColor = Color(0xFFA1A1AA),
                        indicatorColor = TravzRedPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_dashboard")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.SEARCH,
                    onClick = { viewModel.selectTab(AppNavTab.SEARCH) },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Availability") },
                    label = { Text("Availability", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TravzWhite,
                        selectedTextColor = TravzRedPrimary,
                        unselectedIconColor = Color(0xFFA1A1AA),
                        unselectedTextColor = Color(0xFFA1A1AA),
                        indicatorColor = TravzRedPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_search")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.BOOKINGS,
                    onClick = { viewModel.selectTab(AppNavTab.BOOKINGS) },
                    icon = { Icon(Icons.Default.BookOnline, contentDescription = "Bookings") },
                    label = { Text("Bookings", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TravzWhite,
                        selectedTextColor = TravzRedPrimary,
                        unselectedIconColor = Color(0xFFA1A1AA),
                        unselectedTextColor = Color(0xFFA1A1AA),
                        indicatorColor = TravzRedPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_bookings")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.FLEET,
                    onClick = { viewModel.selectTab(AppNavTab.FLEET) },
                    icon = { Icon(Icons.Default.DirectionsCar, contentDescription = "Fleet") },
                    label = { Text("Fleet", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TravzWhite,
                        selectedTextColor = TravzRedPrimary,
                        unselectedIconColor = Color(0xFFA1A1AA),
                        unselectedTextColor = Color(0xFFA1A1AA),
                        indicatorColor = TravzRedPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_fleet")
                )

                NavigationBarItem(
                    selected = currentTab == AppNavTab.CALENDAR,
                    onClick = { viewModel.selectTab(AppNavTab.CALENDAR) },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar") },
                    label = { Text("Calendar", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TravzWhite,
                        selectedTextColor = TravzRedPrimary,
                        unselectedIconColor = Color(0xFFA1A1AA),
                        unselectedTextColor = Color(0xFFA1A1AA),
                        indicatorColor = TravzRedPrimary
                    ),
                    modifier = Modifier.testTag("nav_tab_calendar")
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            when (currentTab) {
                AppNavTab.DASHBOARD -> {
                    DashboardScreen(
                        summary = todaySummary,
                        todayItems = todayAvailability,
                        onQuickSearchClick = { viewModel.selectTab(AppNavTab.SEARCH) },
                        onBookVehicleClick = { vehicle ->
                            val target = todayAvailability.firstOrNull { it.vehicle.id == vehicle.id }
                            viewModel.bookingTargetVehicle.value = target
                        },
                        onViewBookingClick = { booking ->
                            viewModel.selectedBookingForDetails.value = booking
                        },
                        onFilterByStatusClick = {
                            viewModel.selectTab(AppNavTab.SEARCH)
                        }
                    )
                }

                AppNavTab.SEARCH -> {
                    AvailabilitySearchScreen(
                        pickupDate = pickupDate,
                        dropoffDate = dropoffDate,
                        selectedCategory = selectedCategory,
                        searchResults = searchResults,
                        onPickupDateChange = { viewModel.setPickupDate(it) },
                        onDropoffDateChange = { viewModel.setDropoffDate(it) },
                        onCategoryChange = { viewModel.setSelectedCategory(it) },
                        onSearchClick = { viewModel.executeSearch() },
                        onBookVehicleClick = { vehicle ->
                            val target = searchResults.firstOrNull { it.vehicle.id == vehicle.id }
                            viewModel.bookingTargetVehicle.value = target
                        },
                        onViewBookingClick = { booking ->
                            viewModel.selectedBookingForDetails.value = booking
                        }
                    )
                }

                AppNavTab.BOOKINGS -> {
                    BookingsManagementScreen(
                        bookings = allBookings,
                        vehicles = allVehicles,
                        canCancelOrEdit = canCancelOrEdit,
                        onViewBooking = { booking ->
                            viewModel.selectedBookingForDetails.value = booking
                        },
                        onEditBooking = { booking ->
                            viewModel.bookingToEdit.value = booking
                        },
                        onCancelBooking = { id, code ->
                            viewModel.cancelBooking(id, code)
                        },
                        onChangePaymentStatus = { id, status ->
                            viewModel.updatePaymentStatus(id, status)
                        }
                    )
                }

                AppNavTab.FLEET -> {
                    FleetManagementScreen(
                        vehicles = allVehicles,
                        canManageFleet = canManageFleet,
                        onAddVehicleClick = {
                            viewModel.showAddVehicleDialog.value = true
                        },
                        onEditVehicleClick = { vehicle ->
                            viewModel.vehicleToEdit.value = vehicle
                        },
                        onChangeStatus = { vehicleId, newStatus ->
                            viewModel.updateVehicleStatus(vehicleId, newStatus)
                        }
                    )
                }

                AppNavTab.CALENDAR -> {
                    CalendarScreen(
                        selectedDate = calendarDate,
                        onSelectDate = { viewModel.setCalendarDate(it) },
                        dayAvailability = calendarAvailability,
                        onBookVehicle = { vehicle ->
                            val target = calendarAvailability.firstOrNull { it.vehicle.id == vehicle.id }
                            viewModel.bookingTargetVehicle.value = target
                        },
                        onViewBooking = { booking ->
                            viewModel.selectedBookingForDetails.value = booking
                        }
                    )
                }
            }
        }
    }

    // Modal: Book Vehicle Dialog
    bookingTargetVehicle?.let { item ->
        BookingDialog(
            vehicle = item.vehicle,
            initialPickupDate = pickupDate,
            initialDropoffDate = dropoffDate,
            locations = allLocations,
            onDismiss = { viewModel.bookingTargetVehicle.value = null },
            onSubmit = { name, phone, email, vId, pDate, dDate, pTime, dTime, pLoc, dLoc, rate, payStatus, bStatus, notes ->
                viewModel.submitBooking(
                    customerName = name,
                    customerPhone = phone,
                    customerEmail = email,
                    vehicleId = vId,
                    pickupDate = pDate,
                    dropoffDate = dDate,
                    pickupTime = pTime,
                    dropoffTime = dTime,
                    pickupLocation = pLoc,
                    dropoffLocation = dLoc,
                    dailyRate = rate,
                    paymentStatus = payStatus,
                    bookingStatus = bStatus,
                    notes = notes
                )
            }
        )
    }

    // Modal: View Booking Details Dialog
    selectedBookingForDetails?.let { booking ->
        val vehicle = allVehicles.firstOrNull { it.id == booking.vehicleId }
        BookingDetailsDialog(
            booking = booking,
            vehicle = vehicle,
            canCancelOrEdit = canCancelOrEdit,
            onDismiss = { viewModel.selectedBookingForDetails.value = null },
            onCancelBooking = { id, code ->
                viewModel.cancelBooking(id, code)
            },
            onChangePaymentStatus = { id, status ->
                viewModel.updatePaymentStatus(id, status)
            },
            onEditBooking = { b ->
                viewModel.selectedBookingForDetails.value = null
                viewModel.bookingToEdit.value = b
            }
        )
    }

    // Modal: Add or Edit Vehicle Dialog (Fleet)
    if (showAddVehicleDialog) {
        AddEditVehicleDialog(
            vehicle = null,
            onDismiss = { viewModel.showAddVehicleDialog.value = false },
            onSave = { entity, isNew ->
                viewModel.saveVehicle(entity, isNew)
            }
        )
    }

    vehicleToEdit?.let { vehicle ->
        AddEditVehicleDialog(
            vehicle = vehicle,
            onDismiss = { viewModel.vehicleToEdit.value = null },
            onSave = { entity, isNew ->
                viewModel.saveVehicle(entity, isNew)
            }
        )
    }

    // Modal: Staff Login / Switcher Dialog
    if (showStaffSwitcher) {
        StaffSwitcherDialog(
            currentStaff = currentStaff,
            allStaff = allStaff,
            onSelectStaff = { selected ->
                viewModel.switchStaff(selected)
            },
            onDismiss = { viewModel.showStaffSwitcher.value = false }
        )
    }
}
