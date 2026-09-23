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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.repository.DashboardSummary
import com.example.ui.components.SummaryMetricCard
import com.example.ui.components.VehicleItemCard
import com.example.ui.theme.StatusAvailableBg
import com.example.ui.theme.StatusAvailableGreen
import com.example.ui.theme.StatusBookedBg
import com.example.ui.theme.StatusBookedRed
import com.example.ui.theme.StatusMaintenanceBg
import com.example.ui.theme.StatusMaintenanceOrange
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzDarkCard
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@Composable
fun DashboardScreen(
    summary: DashboardSummary,
    todayItems: List<VehicleAvailabilityItem>,
    onQuickSearchClick: () -> Unit,
    onBookVehicleClick: (VehicleEntity) -> Unit,
    onViewBookingClick: (BookingEntity) -> Unit,
    onFilterByStatusClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero / Date Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TravzBlack),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FLEET AVAILABILITY OVERVIEW",
                                color = TravzRedPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Today, ${DateUtils.formatDisplay(DateUtils.today())}",
                                color = TravzWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            color = TravzDarkCard,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF3F3F46))
                        ) {
                            Text(
                                text = "Muscat Fleet",
                                color = TravzWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Search Shortcut Button
                    Button(
                        onClick = onQuickSearchClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("dashboard_search_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TravzRedPrimary,
                            contentColor = TravzWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Search Dates & Check Availability",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // The 4 Summary Cards required by user prompt
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Row 1: Total Vehicles & Available
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryMetricCard(
                        title = "Total Vehicles",
                        count = summary.totalVehicles,
                        accentColor = TravzBlack,
                        bgColor = Color(0xFFF1F5F9),
                        icon = Icons.Default.DirectionsCar,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("card_total_vehicles"),
                        onClick = { onFilterByStatusClick("All") }
                    )

                    SummaryMetricCard(
                        title = "Available",
                        count = summary.available,
                        accentColor = StatusAvailableGreen,
                        bgColor = StatusAvailableBg,
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("card_available_vehicles"),
                        onClick = { onFilterByStatusClick("Available") }
                    )
                }

                // Row 2: Booked & Maintenance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryMetricCard(
                        title = "Booked",
                        count = summary.booked,
                        accentColor = StatusBookedRed,
                        bgColor = StatusBookedBg,
                        icon = Icons.Default.EventBusy,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("card_booked_vehicles"),
                        onClick = { onFilterByStatusClick("Booked") }
                    )

                    SummaryMetricCard(
                        title = "Maintenance",
                        count = summary.maintenance,
                        accentColor = StatusMaintenanceOrange,
                        bgColor = StatusMaintenanceBg,
                        icon = Icons.Default.Build,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("card_maintenance_vehicles"),
                        onClick = { onFilterByStatusClick("Maintenance") }
                    )
                }
            }
        }

        // Section Title: Today's Vehicle Availability
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today's Vehicle Availability",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TravzTextPrimary
                    )
                    Text(
                        text = "${todayItems.size} vehicles registered in fleet",
                        fontSize = 12.sp,
                        color = TravzTextSecondary
                    )
                }

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .clickable { onQuickSearchClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Filter Dates",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TravzRedPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TravzRedPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Today's vehicles list
        items(todayItems, key = { it.vehicle.id }) { item ->
            VehicleItemCard(
                item = item,
                onBookClick = onBookVehicleClick,
                onViewBookingClick = onViewBookingClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
