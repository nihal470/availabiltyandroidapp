package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.VehicleEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzRedLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@Composable
fun BookingsManagementScreen(
    bookings: List<BookingEntity>,
    vehicles: List<VehicleEntity>,
    canCancelOrEdit: Boolean,
    onViewBooking: (BookingEntity) -> Unit,
    onEditBooking: (BookingEntity) -> Unit,
    onCancelBooking: (Long, String) -> Unit,
    onChangePaymentStatus: (Long, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("All") }

    val statusFilters = listOf("All", "Active", "Confirmed", "Pending", "Cancelled")

    val filteredBookings = bookings.filter { booking ->
        val matchesStatus = selectedStatusFilter == "All" ||
                booking.bookingStatus.equals(selectedStatusFilter, ignoreCase = true)
        val vehicle = vehicles.firstOrNull { it.id == booking.vehicleId }
        val matchesQuery = searchQuery.isBlank() ||
                booking.bookingCode.contains(searchQuery, ignoreCase = true) ||
                booking.customerName.contains(searchQuery, ignoreCase = true) ||
                (vehicle?.fullName?.contains(searchQuery, ignoreCase = true) == true)
        matchesStatus && matchesQuery
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("bookings_management_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Search & Title
        item {
            Column {
                Text(
                    text = "Booking Management",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TravzTextPrimary
                )
                Text(
                    text = "Manage reservations, track payments, and review customer bookings",
                    fontSize = 12.sp,
                    color = TravzTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Booking ID, Customer, or Vehicle...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TravzTextSecondary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("bookings_search_bar"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TravzRedPrimary,
                        cursorColor = TravzRedPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Status Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(statusFilters) { filter ->
                        val isSelected = filter == selectedStatusFilter
                        Surface(
                            color = if (isSelected) TravzBlack else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) TravzBlack else TravzBorderLight
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectedStatusFilter = filter }
                                .testTag("booking_filter_$filter")
                        ) {
                            Text(
                                text = filter,
                                color = if (isSelected) TravzWhite else TravzTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (filteredBookings.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = TravzWhite),
                    border = BorderStroke(1.dp, TravzBorderLight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookOnline,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No bookings found",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TravzTextPrimary
                        )
                        Text(
                            text = "Try clearing your search or status filter.",
                            fontSize = 12.sp,
                            color = TravzTextSecondary
                        )
                    }
                }
            }
        } else {
            items(filteredBookings, key = { it.id }) { booking ->
                val vehicle = vehicles.firstOrNull { it.id == booking.vehicleId }
                BookingRowCard(
                    booking = booking,
                    vehicle = vehicle,
                    canCancelOrEdit = canCancelOrEdit,
                    onViewClick = { onViewBooking(booking) },
                    onEditClick = { onEditBooking(booking) },
                    onCancelClick = { onCancelBooking(booking.id, booking.bookingCode) },
                    onChangePayment = { status -> onChangePaymentStatus(booking.id, status) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun BookingRowCard(
    booking: BookingEntity,
    vehicle: VehicleEntity?,
    canCancelOrEdit: Boolean,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onCancelClick: () -> Unit,
    onChangePayment: (String) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onViewClick() }
            .testTag("booking_card_${booking.bookingCode}"),
        colors = CardDefaults.cardColors(containerColor = TravzWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, TravzBorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Booking Code + Badges + Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = booking.bookingCode,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = TravzTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(status = booking.bookingStatus)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(status = booking.paymentStatus)
                    Spacer(modifier = Modifier.width(4.dp))

                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Actions",
                                tint = TravzTextSecondary
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("View Details") },
                                onClick = {
                                    menuExpanded = false
                                    onViewClick()
                                }
                            )

                            if (canCancelOrEdit && !booking.bookingStatus.equals("Cancelled", ignoreCase = true)) {
                                DropdownMenuItem(
                                    text = { Text("Edit Booking") },
                                    onClick = {
                                        menuExpanded = false
                                        onEditClick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Cancel Booking", color = Color(0xFFEF4444)) },
                                    onClick = {
                                        menuExpanded = false
                                        onCancelClick()
                                    }
                                )
                            }

                            // Quick Payment options
                            DropdownMenuItem(
                                text = { Text("Mark as Paid") },
                                onClick = {
                                    menuExpanded = false
                                    onChangePayment("Paid")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Mark as Partial") },
                                onClick = {
                                    menuExpanded = false
                                    onChangePayment("Partial")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Mark as Unpaid") },
                                onClick = {
                                    menuExpanded = false
                                    onChangePayment("Unpaid")
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Customer & Vehicle info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = TravzRedPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.customerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TravzTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = TravzTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = vehicle?.fullName ?: "Vehicle #${booking.vehicleId}",
                            fontSize = 12.sp,
                            color = TravzTextSecondary
                        )
                        if (vehicle != null) {
                            Text(
                                text = " (${vehicle.regNumber})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TravzTextSecondary
                            )
                        }
                    }
                }

                // Amount
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = DateUtils.formatOmr(booking.totalAmount),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = TravzRedPrimary
                    )
                    Text(
                        text = "${booking.rentalDays} Day${if (booking.rentalDays > 1) "s" else ""}",
                        fontSize = 11.sp,
                        color = TravzTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Dates & Locations
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = TravzTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${DateUtils.formatDisplay(booking.pickupDate)} → ${DateUtils.formatDisplay(booking.dropoffDate)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TravzTextPrimary
                    )
                }
            }
        }
    }
}
