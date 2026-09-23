package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingEntity
import com.example.data.model.StaffUserEntity
import com.example.data.model.VehicleAvailabilityItem
import com.example.data.model.VehicleEntity
import com.example.data.model.VehicleStatus
import com.example.ui.theme.StatusAvailableBg
import com.example.ui.theme.StatusAvailableGreen
import com.example.ui.theme.StatusBookedBg
import com.example.ui.theme.StatusBookedRed
import com.example.ui.theme.StatusIdleBg
import com.example.ui.theme.StatusIdleGray
import com.example.ui.theme.StatusMaintenanceBg
import com.example.ui.theme.StatusMaintenanceOrange
import com.example.ui.theme.StatusPaidGreen
import com.example.ui.theme.StatusPendingYellow
import com.example.ui.theme.StatusUnpaidRed
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzDarkCard
import com.example.ui.theme.TravzRedLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextMuted
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@Composable
fun TravzTopHeader(
    currentStaff: StaffUserEntity,
    onStaffClick: () -> Unit
) {
    Surface(
        color = TravzBlack,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Red badge
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TravzRedPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Travz Logo",
                        tint = TravzWhite,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TRAVZ",
                            color = TravzRedPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "CAR RENTAL",
                            color = TravzWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "Availability & Fleet System",
                        color = Color(0xFFA1A1AA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Staff profile button
            Surface(
                color = TravzDarkCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF3F3F46)),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onStaffClick() }
                    .testTag("staff_profile_button")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when (currentStaff.role) {
                                    "Admin" -> TravzRedPrimary
                                    "Manager" -> Color(0xFF2563EB)
                                    else -> Color(0xFF059669)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentStaff.role.take(1),
                            color = TravzWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = currentStaff.name.split(" ").firstOrNull() ?: currentStaff.name,
                            color = TravzWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = currentStaff.role,
                            color = Color(0xFFA1A1AA),
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch Staff",
                        tint = Color(0xFFA1A1AA),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "available" -> Pair(StatusAvailableBg, StatusAvailableGreen)
        "booked" -> Pair(StatusBookedBg, StatusBookedRed)
        "maintenance" -> Pair(StatusMaintenanceBg, StatusMaintenanceOrange)
        "sold" -> Pair(StatusIdleBg, StatusIdleGray)
        "idle" -> Pair(StatusIdleBg, StatusIdleGray)
        "paid" -> Pair(Color(0xFFD1FAE5), StatusPaidGreen)
        "partial" -> Pair(Color(0xFFFEF3C7), StatusPendingYellow)
        "unpaid" -> Pair(Color(0xFFFFE4E6), StatusUnpaidRed)
        "confirmed" -> Pair(Color(0xFFDBEAFE), Color(0xFF1D4ED8))
        "active" -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
        "cancelled" -> Pair(Color(0xFFF1F5F9), Color(0xFF64748B))
        "pending" -> Pair(Color(0xFFFEF9C3), Color(0xFFA16207))
        else -> Pair(StatusIdleBg, StatusIdleGray)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = status,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SummaryMetricCard(
    title: String,
    count: Int,
    accentColor: Color,
    bgColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .let { if (onClick != null) it.clickable { onClick() } else it },
        colors = CardDefaults.cardColors(containerColor = TravzWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, TravzBorderLight)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = count.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = TravzTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TravzTextSecondary
            )
        }
    }
}

@Composable
fun VehicleItemCard(
    item: VehicleAvailabilityItem,
    onBookClick: (VehicleEntity) -> Unit,
    onViewBookingClick: ((BookingEntity) -> Unit)? = null,
    onEditVehicleClick: ((VehicleEntity) -> Unit)? = null,
    canEditFleet: Boolean = false,
    modifier: Modifier = Modifier
) {
    val vehicle = item.vehicle
    val context = LocalContext.current

    // Resolve local drawable resource if available
    val imageResId = if (vehicle.imageDrawableName.isNotBlank()) {
        val id = context.resources.getIdentifier(
            vehicle.imageDrawableName,
            "drawable",
            context.packageName
        )
        if (id != 0) id else null
    } else null

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = TravzWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            when (item.calculatedStatus) {
                VehicleStatus.AVAILABLE -> Color(0xFFBBF7D0)
                VehicleStatus.BOOKED -> Color(0xFFFECACA)
                VehicleStatus.MAINTENANCE -> Color(0xFFFED7AA)
                else -> TravzBorderLight
            }
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Vehicle Image / Header Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF1E1E24))
            ) {
                if (imageResId != null) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = vehicle.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(140.dp)
                    )
                } else {
                    // Fallback visual illustration
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(Color(0xFF27272A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = vehicle.fullName,
                            tint = TravzRedLight,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }

                // Top Chips (Category + Status)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = TravzBlack.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "${vehicle.category} • ${vehicle.modelYear}",
                            color = TravzWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    StatusBadge(status = item.calculatedStatus.label)
                }

                // Price Tag Pill Bottom Right
                Surface(
                    color = TravzRedPrimary,
                    shape = RoundedCornerShape(topStart = 10.dp),
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Text(
                        text = "${DateUtils.formatOmr(vehicle.dailyRate)} / day",
                        color = TravzWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            // Vehicle Details
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = vehicle.fullName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TravzTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                                text = "${vehicle.transmission} • ${vehicle.seats} Seats",
                                fontSize = 12.sp,
                                color = TravzTextSecondary
                            )
                        }
                    }

                    if (canEditFleet && onEditVehicleClick != null) {
                        OutlinedButton(
                            onClick = { onEditVehicleClick(vehicle) },
                            modifier = Modifier.height(34.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Vehicle",
                                modifier = Modifier.size(14.dp),
                                tint = TravzTextSecondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp, color = TravzTextSecondary)
                        }
                    }
                }

                // If Booked or Maintenance: show banner
                if (item.calculatedStatus == VehicleStatus.BOOKED && item.booking != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFFFF1F2),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Booked by: ${item.booking.customerName}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF9F1239)
                                )
                                Text(
                                    text = "${DateUtils.formatDisplay(item.booking.pickupDate)} - ${DateUtils.formatDisplay(item.booking.dropoffDate)}",
                                    fontSize = 11.sp,
                                    color = Color(0xFFBE123C)
                                )
                            }

                            if (onViewBookingClick != null) {
                                OutlinedButton(
                                    onClick = { onViewBookingClick(item.booking) },
                                    modifier = Modifier.height(32.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = TravzRedPrimary
                                    ),
                                    border = BorderStroke(1.dp, TravzRedPrimary),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text("Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else if (item.calculatedStatus == VehicleStatus.MAINTENANCE && item.maintenance != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFFFFBEB),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Service: ${item.maintenance.reason}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Scheduled: ${DateUtils.formatDisplay(item.maintenance.startDate)} - ${DateUtils.formatDisplay(item.maintenance.endDate)}",
                                fontSize = 11.sp,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                // Action Area
                Spacer(modifier = Modifier.height(12.dp))
                if (item.calculatedStatus == VehicleStatus.AVAILABLE) {
                    Button(
                        onClick = { onBookClick(vehicle) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("book_vehicle_${vehicle.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TravzRedPrimary,
                            contentColor = TravzWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CarRental,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Book Vehicle",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else if (item.calculatedStatus == VehicleStatus.BOOKED && item.booking != null && onViewBookingClick != null) {
                    OutlinedButton(
                        onClick = { onViewBookingClick(item.booking) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, TravzBorderLight)
                    ) {
                        Text(
                            text = "View Active Booking",
                            color = TravzTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
