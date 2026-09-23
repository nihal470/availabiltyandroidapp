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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.ui.components.StatusBadge
import com.example.ui.components.VehicleItemCard
import com.example.ui.theme.StatusAvailableBg
import com.example.ui.theme.StatusAvailableGreen
import com.example.ui.theme.StatusBookedBg
import com.example.ui.theme.StatusBookedRed
import com.example.ui.theme.StatusMaintenanceBg
import com.example.ui.theme.StatusMaintenanceOrange
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzRedLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@Composable
fun CalendarScreen(
    selectedDate: String,
    onSelectDate: (String) -> Unit,
    dayAvailability: List<VehicleAvailabilityItem>,
    onBookVehicle: (VehicleEntity) -> Unit,
    onViewBooking: (BookingEntity) -> Unit
) {
    // Generate dates: 5 days before, 14 days after selectedDate
    val dateList = remember(selectedDate) {
        val baseDate = DateUtils.today()
        (-3..14).map { offset ->
            DateUtils.addDays(baseDate, offset)
        }
    }

    val availableCount = dayAvailability.count { it.calculatedStatus == VehicleStatus.AVAILABLE }
    val bookedCount = dayAvailability.count { it.calculatedStatus == VehicleStatus.BOOKED }
    val maintenanceCount = dayAvailability.count { it.calculatedStatus == VehicleStatus.MAINTENANCE }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("calendar_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Calendar Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Availability Calendar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TravzTextPrimary
                        )
                        Text(
                            text = "Track fleet occupancy and maintenance across dates",
                            fontSize = 12.sp,
                            color = TravzTextSecondary
                        )
                    }

                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .clickable { onSelectDate(DateUtils.today()) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Today",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TravzRedPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Date Navigation Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onSelectDate(DateUtils.addDays(selectedDate, -1)) }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Day")
                    }

                    Text(
                        text = DateUtils.formatDisplay(selectedDate),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = TravzTextPrimary
                    )

                    IconButton(
                        onClick = { onSelectDate(DateUtils.addDays(selectedDate, 1)) }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Day")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal Days Strip
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(dateList) { date ->
                        val isSelected = date == selectedDate
                        val isToday = date == DateUtils.today()

                        val parts = date.split("-")
                        val dayNumber = parts.getOrNull(2) ?: ""

                        Surface(
                            color = when {
                                isSelected -> TravzBlack
                                isToday -> Color(0xFFFEF2F2)
                                else -> Color(0xFFF8FAFC)
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                when {
                                    isSelected -> TravzBlack
                                    isToday -> TravzRedPrimary
                                    else -> TravzBorderLight
                                }
                            ),
                            modifier = Modifier
                                .width(54.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectDate(date) }
                                .testTag("calendar_day_$date")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (isToday) "TODAY" else DateUtils.formatDisplay(date).take(3).uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) TravzRedPrimary else TravzTextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = dayNumber,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) TravzWhite else TravzTextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Status Metrics
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TravzWhite),
                border = BorderStroke(1.dp, TravzBorderLight),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = availableCount.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = StatusAvailableGreen
                        )
                        Text(
                            text = "Available",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TravzTextSecondary
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(TravzBorderLight))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = bookedCount.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = StatusBookedRed
                        )
                        Text(
                            text = "Booked",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TravzTextSecondary
                        )
                    }

                    Box(modifier = Modifier.width(1.dp).height(28.dp).background(TravzBorderLight))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = maintenanceCount.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = StatusMaintenanceOrange
                        )
                        Text(
                            text = "Maintenance",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TravzTextSecondary
                        )
                    }
                }
            }
        }

        // Vehicles on this date
        item {
            Text(
                text = "Fleet Status on ${DateUtils.formatDisplay(selectedDate)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TravzTextPrimary
            )
        }

        items(dayAvailability, key = { it.vehicle.id }) { item ->
            VehicleItemCard(
                item = item,
                onBookClick = onBookVehicle,
                onViewBookingClick = onViewBooking
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
