package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.VehicleEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@Composable
fun FleetManagementScreen(
    vehicles: List<VehicleEntity>,
    canManageFleet: Boolean,
    onAddVehicleClick: () -> Unit,
    onEditVehicleClick: (VehicleEntity) -> Unit,
    onChangeStatus: (Long, String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var selectedStatusFilter by remember { mutableStateOf("All") }

    val categories = listOf("All", "Economy", "Sedan", "SUV", "4x4")
    val statuses = listOf("All", "Available", "Booked", "Maintenance", "Sold", "Idle")

    val filteredVehicles = vehicles.filter { v ->
        val matchesCategory = selectedCategoryFilter == "All" || v.category.equals(selectedCategoryFilter, ignoreCase = true)
        val matchesStatus = selectedStatusFilter == "All" || v.status.equals(selectedStatusFilter, ignoreCase = true)
        val matchesQuery = searchQuery.isBlank() ||
                v.fullName.contains(searchQuery, ignoreCase = true) ||
                v.regNumber.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesStatus && matchesQuery
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("fleet_management_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fleet Management",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TravzTextPrimary
                    )
                    Text(
                        text = "${vehicles.size} Vehicles in Oman Registry",
                        fontSize = 12.sp,
                        color = TravzTextSecondary
                    )
                }

                if (canManageFleet) {
                    Button(
                        onClick = onAddVehicleClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TravzRedPrimary,
                            contentColor = TravzWhite
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("add_vehicle_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Vehicle", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Search Bar & Filter Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by make, model, or plate...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = TravzTextSecondary)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fleet_search_bar"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TravzRedPrimary,
                        cursorColor = TravzRedPrimary
                    )
                )

                // Category filters
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val isSelected = cat == selectedCategoryFilter
                        Surface(
                            color = if (isSelected) TravzBlack else Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, if (isSelected) TravzBlack else TravzBorderLight),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedCategoryFilter = cat }
                        ) {
                            Text(
                                text = cat,
                                color = if (isSelected) TravzWhite else TravzTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Status filters
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(statuses) { st ->
                        val isSelected = st == selectedStatusFilter
                        Surface(
                            color = if (isSelected) Color(0xFFDC2626) else Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFFDC2626) else TravzBorderLight),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedStatusFilter = st }
                        ) {
                            Text(
                                text = st,
                                color = if (isSelected) TravzWhite else TravzTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Vehicles list
        items(filteredVehicles, key = { it.id }) { vehicle ->
            FleetVehicleCard(
                vehicle = vehicle,
                canManageFleet = canManageFleet,
                onEditClick = { onEditVehicleClick(vehicle) },
                onChangeStatus = { newStatus -> onChangeStatus(vehicle.id, newStatus) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun FleetVehicleCard(
    vehicle: VehicleEntity,
    canManageFleet: Boolean,
    onEditClick: () -> Unit,
    onChangeStatus: (String) -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = TravzWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, TravzBorderLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = vehicle.fullName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TravzTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Text(
                                text = vehicle.regNumber,
                                color = Color(0xFF1E293B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${vehicle.category} • ${vehicle.modelYear}",
                            fontSize = 12.sp,
                            color = TravzTextSecondary
                        )
                    }
                }

                // Daily Rate
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = DateUtils.formatOmr(vehicle.dailyRate),
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = TravzRedPrimary
                    )
                    Text(
                        text = "per day",
                        fontSize = 10.sp,
                        color = TravzTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Specs row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${vehicle.transmission} • ${vehicle.seats} Seats • ${vehicle.fuelType}",
                    fontSize = 11.sp,
                    color = TravzTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Status and Management Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status chip with menu
                Box {
                    Surface(
                        color = Color.Transparent,
                        modifier = Modifier
                            .clickable(enabled = canManageFleet) { statusMenuExpanded = true }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusBadge(status = vehicle.status)
                            if (canManageFleet) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Change ▼",
                                    fontSize = 10.sp,
                                    color = TravzTextSecondary
                                )
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        listOf("Available", "Booked", "Maintenance", "Sold", "Idle").forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st) },
                                onClick = {
                                    statusMenuExpanded = false
                                    onChangeStatus(st)
                                }
                            )
                        }
                    }
                }

                if (canManageFleet) {
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = TravzTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit Specs",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TravzTextPrimary
                        )
                    }
                }
            }
        }
    }
}
