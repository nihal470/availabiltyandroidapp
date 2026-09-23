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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.VehicleAvailabilityItem
import com.example.data.model.VehicleEntity
import com.example.data.model.VehicleStatus
import com.example.ui.components.VehicleItemCard
import com.example.ui.theme.StatusAvailableGreen
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzDarkCard
import com.example.ui.theme.TravzRedLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@Composable
fun AvailabilitySearchScreen(
    pickupDate: String,
    dropoffDate: String,
    selectedCategory: String,
    searchResults: List<VehicleAvailabilityItem>,
    onPickupDateChange: (String) -> Unit,
    onDropoffDateChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onBookVehicleClick: (VehicleEntity) -> Unit,
    onViewBookingClick: (BookingEntity) -> Unit
) {
    val categories = listOf("All", "Economy", "Sedan", "SUV", "4x4")
    val rentalDays = DateUtils.calculateDays(pickupDate, dropoffDate)

    val availableCount = searchResults.count { it.calculatedStatus == VehicleStatus.AVAILABLE }
    val bookedCount = searchResults.count { it.calculatedStatus == VehicleStatus.BOOKED }
    val maintenanceCount = searchResults.count { it.calculatedStatus == VehicleStatus.MAINTENANCE }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("availability_search_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Prominent Search Section Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TravzWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = BorderStroke(1.dp, TravzBorderLight),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(TravzRedLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TravzRedPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Availability Search",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = TravzTextPrimary
                            )
                            Text(
                                text = "Select dates and vehicle category to verify real-time availability",
                                fontSize = 11.sp,
                                color = TravzTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Date Pickers Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = pickupDate,
                            onValueChange = onPickupDateChange,
                            label = { Text("Pickup Date") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = TravzRedPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("search_pickup_date_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )

                        OutlinedTextField(
                            value = dropoffDate,
                            onValueChange = onDropoffDateChange,
                            label = { Text("Drop-off Date") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = TravzRedPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("search_dropoff_date_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick presets row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetChip(
                            label = "Today (3d)",
                            onClick = {
                                onPickupDateChange(DateUtils.today())
                                onDropoffDateChange(DateUtils.addDays(DateUtils.today(), 3))
                            }
                        )
                        PresetChip(
                            label = "Tomorrow (5d)",
                            onClick = {
                                onPickupDateChange(DateUtils.addDays(DateUtils.today(), 1))
                                onDropoffDateChange(DateUtils.addDays(DateUtils.today(), 6))
                            }
                        )
                        PresetChip(
                            label = "Next Week (7d)",
                            onClick = {
                                onPickupDateChange(DateUtils.addDays(DateUtils.today(), 7))
                                onDropoffDateChange(DateUtils.addDays(DateUtils.today(), 14))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Category Filter Pills
                    Text(
                        text = "Vehicle Category",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TravzTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSelected = cat.equals(selectedCategory, ignoreCase = true)
                            Surface(
                                color = if (isSelected) TravzBlack else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) TravzBlack else TravzBorderLight
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onCategoryChange(cat) }
                                    .testTag("filter_cat_$cat")
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) TravzWhite else TravzTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Button
                    Button(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("search_availability_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TravzRedPrimary,
                            contentColor = TravzWhite
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search Availability ($rentalDays Day${if (rentalDays > 1) "s" else ""})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Search Results Summary Banner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Results for ${DateUtils.formatDisplay(pickupDate)} → ${DateUtils.formatDisplay(dropoffDate)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TravzTextPrimary
                    )
                    Text(
                        text = "$availableCount Available • $bookedCount Booked • $maintenanceCount Maintenance",
                        fontSize = 12.sp,
                        color = TravzTextSecondary
                    )
                }

                Surface(
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "$availableCount Ready",
                        color = StatusAvailableGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Vehicles List
        if (searchResults.isEmpty()) {
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
                            imageVector = Icons.Default.CarRental,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No vehicles found",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TravzTextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your dates or selecting a different category.",
                            fontSize = 13.sp,
                            color = TravzTextSecondary
                        )
                    }
                }
            }
        } else {
            items(searchResults, key = { it.vehicle.id }) { item ->
                VehicleItemCard(
                    item = item,
                    onBookClick = onBookVehicleClick,
                    onViewBookingClick = onViewBookingClick
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PresetChip(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = TravzTextSecondary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
