package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
fun BookingDetailsDialog(
    booking: BookingEntity,
    vehicle: VehicleEntity?,
    canCancelOrEdit: Boolean,
    onDismiss: () -> Unit,
    onCancelBooking: (Long, String) -> Unit,
    onChangePaymentStatus: (Long, String) -> Unit,
    onEditBooking: (BookingEntity) -> Unit
) {
    var showCancelConfirm by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp)),
            color = TravzWhite,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TravzBlack)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = booking.bookingCode,
                                color = TravzWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Booking Details & Status",
                                color = TravzRedLight,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TravzWhite)
                        }
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Status row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Booking: ",
                                fontSize = 12.sp,
                                color = TravzTextSecondary
                            )
                            StatusBadge(status = booking.bookingStatus)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Payment: ",
                                fontSize = 12.sp,
                                color = TravzTextSecondary
                            )
                            StatusBadge(status = booking.paymentStatus)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Vehicle Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, TravzBorderLight)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TravzRedLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = null,
                                    tint = TravzRedPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = vehicle?.fullName ?: "Vehicle #${booking.vehicleId}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TravzTextPrimary
                                )
                                Text(
                                    text = "Reg: ${vehicle?.regNumber ?: "N/A"} • ${vehicle?.category ?: "Fleet"} • ${vehicle?.modelYear ?: ""}",
                                    fontSize = 12.sp,
                                    color = TravzTextSecondary
                                )
                            }
                            Text(
                                text = "${DateUtils.formatOmr(booking.dailyRate)}/d",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = TravzRedPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Customer info
                    DetailSection(title = "Customer Information") {
                        DetailItem(icon = Icons.Default.Person, label = "Name", value = booking.customerName)
                        DetailItem(icon = Icons.Default.Phone, label = "Phone", value = booking.customerPhone)
                        if (booking.customerEmail.isNotBlank()) {
                            DetailItem(icon = Icons.Default.Email, label = "Email", value = booking.customerEmail)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Schedule & Locations
                    DetailSection(title = "Rental Schedule & Locations") {
                        DetailItem(
                            icon = Icons.Default.Schedule,
                            label = "Pickup",
                            value = "${DateUtils.formatDisplay(booking.pickupDate)} at ${booking.pickupTime}"
                        )
                        DetailItem(
                            icon = Icons.Default.LocationOn,
                            label = "Pickup Location",
                            value = booking.pickupLocation
                        )
                        DetailItem(
                            icon = Icons.Default.Schedule,
                            label = "Drop-off",
                            value = "${DateUtils.formatDisplay(booking.dropoffDate)} at ${booking.dropoffTime}"
                        )
                        DetailItem(
                            icon = Icons.Default.LocationOn,
                            label = "Drop-off Location",
                            value = booking.dropoffLocation
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Payment & Financial breakdown
                    DetailSection(title = "Financial Summary") {
                        PriceRow(
                            label = "Rental Duration",
                            value = "${booking.rentalDays} Day${if (booking.rentalDays > 1) "s" else ""}"
                        )
                        PriceRow(
                            label = "Subtotal (${DateUtils.formatOmr(booking.dailyRate)} × ${booking.rentalDays})",
                            value = DateUtils.formatOmr(booking.subtotal)
                        )
                        PriceRow(
                            label = "VAT (5%)",
                            value = DateUtils.formatOmr(booking.vatAmount)
                        )
                        PriceRow(
                            label = "Total Amount",
                            value = DateUtils.formatOmr(booking.totalAmount),
                            isBold = true,
                            color = TravzRedPrimary
                        )
                    }

                    if (booking.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailSection(title = "Notes") {
                            Text(
                                text = booking.notes,
                                fontSize = 12.sp,
                                color = TravzTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Change Payment Status Quick Buttons
                    Text(
                        text = "Quick Update Payment Status",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TravzTextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Paid", "Partial", "Unpaid").forEach { status ->
                            val isSelected = booking.paymentStatus.equals(status, ignoreCase = true)
                            OutlinedButton(
                                onClick = { onChangePaymentStatus(booking.id, status) },
                                modifier = Modifier.weight(1f).height(38.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSelected) TravzBlack else TravzWhite,
                                    contentColor = if (isSelected) TravzWhite else TravzTextPrimary
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) TravzBlack else TravzBorderLight
                                )
                            ) {
                                Text(status, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Actions (Edit / Cancel)
                    if (canCancelOrEdit && !booking.bookingStatus.equals("Cancelled", ignoreCase = true)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onEditBooking(booking) },
                                modifier = Modifier.weight(1f).height(46.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Edit Booking", fontSize = 13.sp)
                            }

                            if (!showCancelConfirm) {
                                Button(
                                    onClick = { showCancelConfirm = true },
                                    modifier = Modifier.weight(1f).height(46.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFEF4444),
                                        contentColor = TravzWhite
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Cancel", fontSize = 13.sp)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        onCancelBooking(booking.id, booking.bookingCode)
                                        showCancelConfirm = false
                                    },
                                    modifier = Modifier.weight(1f).height(46.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFB91C1C),
                                        contentColor = TravzWhite
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Confirm Cancel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        color = Color(0xFFFAFAFA),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, TravzBorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TravzTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TravzRedPrimary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            fontSize = 12.sp,
            color = TravzTextSecondary
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TravzTextPrimary
        )
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    color: Color = TravzTextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isBold) TravzTextPrimary else TravzTextSecondary,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            fontSize = if (isBold) 14.sp else 12.sp,
            fontWeight = if (isBold) FontWeight.Black else FontWeight.SemiBold,
            color = color
        )
    }
}
