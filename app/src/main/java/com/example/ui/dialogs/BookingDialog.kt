package com.example.ui.dialogs

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.LocationEntity
import com.example.data.model.VehicleEntity
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzBorderLight
import com.example.ui.theme.TravzRedLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextMuted
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzTextSecondary
import com.example.ui.theme.TravzWhite
import com.example.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDialog(
    vehicle: VehicleEntity,
    initialPickupDate: String,
    initialDropoffDate: String,
    locations: List<LocationEntity>,
    onDismiss: () -> Unit,
    onSubmit: (
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
    ) -> Unit
) {
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("+968 ") }
    var customerEmail by remember { mutableStateOf("") }

    var pickupDate by remember { mutableStateOf(initialPickupDate) }
    var dropoffDate by remember { mutableStateOf(initialDropoffDate) }
    var pickupTime by remember { mutableStateOf("10:00 AM") }
    var dropoffTime by remember { mutableStateOf("10:00 AM") }

    val defaultLoc = locations.firstOrNull()?.name ?: "Muscat International Airport"
    var pickupLocation by remember { mutableStateOf(defaultLoc) }
    var dropoffLocation by remember { mutableStateOf(defaultLoc) }

    var paymentStatus by remember { mutableStateOf("Paid") }
    var bookingStatus by remember { mutableStateOf("Confirmed") }
    var notes by remember { mutableStateOf("") }

    // Automatic calculation of days, subtotal, VAT 5%, total
    val rentalDays = remember(pickupDate, dropoffDate) {
        DateUtils.calculateDays(pickupDate, dropoffDate)
    }
    val rentalSubtotal = remember(rentalDays, vehicle.dailyRate) {
        rentalDays * vehicle.dailyRate
    }
    val vatAmount = remember(rentalSubtotal) {
        rentalSubtotal * 0.05
    }
    val totalAmount = remember(rentalSubtotal, vatAmount) {
        rentalSubtotal + vatAmount
    }

    var pickupLocExpanded by remember { mutableStateOf(false) }
    var dropoffLocExpanded by remember { mutableStateOf(false) }
    var paymentStatusExpanded by remember { mutableStateOf(false) }
    var bookingStatusExpanded by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }

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
                                text = "New Vehicle Booking",
                                color = TravzWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Travz Car Rental • Oman",
                                color = TravzRedLight,
                                fontSize = 12.sp
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_booking_dialog")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TravzWhite
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    // Vehicle Info Banner
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
                                    .size(40.dp)
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
                                    text = vehicle.fullName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TravzTextPrimary
                                )
                                Text(
                                    text = "${vehicle.regNumber} • ${vehicle.category} • ${vehicle.modelYear}",
                                    fontSize = 12.sp,
                                    color = TravzTextSecondary
                                )
                            }
                            Text(
                                text = "${DateUtils.formatOmr(vehicle.dailyRate)}/d",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = TravzRedPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section: Customer Details
                    Text(
                        text = "Customer Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TravzTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Customer Full Name *") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TravzTextMuted)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("booking_customer_name"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TravzRedPrimary,
                            cursorColor = TravzRedPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { customerPhone = it },
                            label = { Text("Phone Number *") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = TravzTextMuted)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("booking_customer_phone"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = customerEmail,
                            onValueChange = { customerEmail = it },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = TravzTextMuted)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("booking_customer_email"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section: Schedule & Locations
                    Text(
                        text = "Rental Schedule & Locations",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TravzTextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = pickupDate,
                            onValueChange = { pickupDate = it },
                            label = { Text("Pickup Date (YYYY-MM-DD)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = dropoffDate,
                            onValueChange = { dropoffDate = it },
                            label = { Text("Drop-off Date (YYYY-MM-DD)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = pickupTime,
                            onValueChange = { pickupTime = it },
                            label = { Text("Pickup Time") },
                            leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = TravzTextMuted) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = dropoffTime,
                            onValueChange = { dropoffTime = it },
                            label = { Text("Drop-off Time") },
                            leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = TravzTextMuted) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pickup Location dropdown
                    ExposedDropdownMenuBox(
                        expanded = pickupLocExpanded,
                        onExpandedChange = { pickupLocExpanded = !pickupLocExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = pickupLocation,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Pickup Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TravzRedPrimary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pickupLocExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = pickupLocExpanded,
                            onDismissRequest = { pickupLocExpanded = false }
                        ) {
                            locations.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc.name) },
                                    onClick = {
                                        pickupLocation = loc.name
                                        pickupLocExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Drop-off Location dropdown
                    ExposedDropdownMenuBox(
                        expanded = dropoffLocExpanded,
                        onExpandedChange = { dropoffLocExpanded = !dropoffLocExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = dropoffLocation,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Drop-off Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TravzRedPrimary) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropoffLocExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = dropoffLocExpanded,
                            onDismissRequest = { dropoffLocExpanded = false }
                        ) {
                            locations.forEach { loc ->
                                DropdownMenuItem(
                                    text = { Text(loc.name) },
                                    onClick = {
                                        dropoffLocation = loc.name
                                        dropoffLocExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Status and Payment
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Payment Status
                        ExposedDropdownMenuBox(
                            expanded = paymentStatusExpanded,
                            onExpandedChange = { paymentStatusExpanded = !paymentStatusExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = paymentStatus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Payment Status") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentStatusExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = paymentStatusExpanded,
                                onDismissRequest = { paymentStatusExpanded = false }
                            ) {
                                listOf("Paid", "Partial", "Unpaid").forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status) },
                                        onClick = {
                                            paymentStatus = status
                                            paymentStatusExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Booking Status
                        ExposedDropdownMenuBox(
                            expanded = bookingStatusExpanded,
                            onExpandedChange = { bookingStatusExpanded = !bookingStatusExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = bookingStatus,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Booking Status") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bookingStatusExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = bookingStatusExpanded,
                                onDismissRequest = { bookingStatusExpanded = false }
                            ) {
                                listOf("Confirmed", "Pending", "Active").forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status) },
                                        onClick = {
                                            bookingStatus = status
                                            bookingStatusExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes & Special Requests") },
                        leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = TravzTextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Calculation Summary Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Pricing Breakdown",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TravzTextPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Daily Rate (${DateUtils.formatOmr(vehicle.dailyRate)} × $rentalDays day${if (rentalDays > 1) "s" else ""})",
                                    fontSize = 12.sp,
                                    color = TravzTextSecondary
                                )
                                Text(
                                    text = DateUtils.formatOmr(rentalSubtotal),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TravzTextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "VAT (5%)",
                                    fontSize = 12.sp,
                                    color = TravzTextSecondary
                                )
                                Text(
                                    text = DateUtils.formatOmr(vatAmount),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TravzTextPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color(0xFFCBD5E1))
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Rental Amount",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TravzTextPrimary
                                )
                                Text(
                                    text = DateUtils.formatOmr(totalAmount),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TravzRedPrimary
                                )
                            }
                        }
                    }

                    if (errorMsg != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMsg ?: "",
                            color = TravzRedPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            if (customerName.isBlank()) {
                                errorMsg = "Please enter customer name."
                                return@Button
                            }
                            if (customerPhone.isBlank() || customerPhone.length < 7) {
                                errorMsg = "Please enter a valid phone number."
                                return@Button
                            }
                            errorMsg = null
                            onSubmit(
                                customerName.trim(),
                                customerPhone.trim(),
                                customerEmail.trim(),
                                vehicle.id,
                                pickupDate,
                                dropoffDate,
                                pickupTime,
                                dropoffTime,
                                pickupLocation,
                                dropoffLocation,
                                vehicle.dailyRate,
                                paymentStatus,
                                bookingStatus,
                                notes
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_booking_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TravzRedPrimary,
                            contentColor = TravzWhite
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Confirm & Reserve Vehicle (${DateUtils.formatOmr(totalAmount)})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
