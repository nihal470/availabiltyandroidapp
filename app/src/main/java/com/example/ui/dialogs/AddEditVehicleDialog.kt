package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.data.model.VehicleEntity
import com.example.ui.theme.TravzBlack
import com.example.ui.theme.TravzRedLight
import com.example.ui.theme.TravzRedPrimary
import com.example.ui.theme.TravzTextPrimary
import com.example.ui.theme.TravzWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditVehicleDialog(
    vehicle: VehicleEntity?, // null if adding new
    onDismiss: () -> Unit,
    onSave: (VehicleEntity, Boolean) -> Unit
) {
    val isNew = (vehicle == null)

    var make by remember { mutableStateOf(vehicle?.make ?: "") }
    var model by remember { mutableStateOf(vehicle?.model ?: "") }
    var regNumber by remember { mutableStateOf(vehicle?.regNumber ?: "") }
    var category by remember { mutableStateOf(vehicle?.category ?: "SUV") }
    var modelYear by remember { mutableStateOf((vehicle?.modelYear ?: 2024).toString()) }
    var dailyRate by remember { mutableStateOf((vehicle?.dailyRate ?: 35.0).toString()) }
    var status by remember { mutableStateOf(vehicle?.status ?: "Available") }
    var transmission by remember { mutableStateOf(vehicle?.transmission ?: "Automatic") }
    var seats by remember { mutableStateOf((vehicle?.seats ?: 5).toString()) }
    var fuelType by remember { mutableStateOf(vehicle?.fuelType ?: "Petrol") }

    var categoryExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

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
                                text = if (isNew) "Add Vehicle to Fleet" else "Edit Vehicle Details",
                                color = TravzWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Travz Fleet Management",
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
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = make,
                            onValueChange = { make = it },
                            label = { Text("Make (e.g. Toyota)") },
                            modifier = Modifier.weight(1f).testTag("vehicle_make_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = model,
                            onValueChange = { model = it },
                            label = { Text("Model (e.g. Prado TXL)") },
                            modifier = Modifier.weight(1f).testTag("vehicle_model_input"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = regNumber,
                            onValueChange = { regNumber = it },
                            label = { Text("Registration # (e.g. 48291-B)") },
                            modifier = Modifier.weight(1f).testTag("vehicle_reg_input"),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = modelYear,
                            onValueChange = { modelYear = it },
                            label = { Text("Model Year") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Category Dropdown
                        ExposedDropdownMenuBox(
                            expanded = categoryExpanded,
                            onExpandedChange = { categoryExpanded = !categoryExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false }
                            ) {
                                listOf("Economy", "Sedan", "SUV", "4x4").forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            category = cat
                                            categoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Daily Rate
                        OutlinedTextField(
                            value = dailyRate,
                            onValueChange = { dailyRate = it },
                            label = { Text("Daily Rate (OMR)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f).testTag("vehicle_rate_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TravzRedPrimary,
                                cursorColor = TravzRedPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Vehicle Status Dropdown
                    ExposedDropdownMenuBox(
                        expanded = statusExpanded,
                        onExpandedChange = { statusExpanded = !statusExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = status,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Vehicle Status") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            listOf("Available", "Booked", "Maintenance", "Sold", "Idle").forEach { st ->
                                DropdownMenuItem(
                                    text = { Text(st) },
                                    onClick = {
                                        status = st
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = transmission,
                            onValueChange = { transmission = it },
                            label = { Text("Transmission") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = seats,
                            onValueChange = { seats = it },
                            label = { Text("Seats") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = fuelType,
                        onValueChange = { fuelType = it },
                        label = { Text("Fuel Type (e.g. Petrol, V8, Twin Turbo)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

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

                    // Assign photo based on category if new
                    val defaultPhoto = when (category) {
                        "4x4" -> "car_4x4_patrol_1790193195572"
                        "Sedan" -> "car_sedan_camry_1790193184188"
                        "Economy" -> "car_economy_accent_1790193207687"
                        else -> "car_suv_prado_1790193171736"
                    }

                    Button(
                        onClick = {
                            if (make.isBlank() || model.isBlank()) {
                                errorMsg = "Please enter both vehicle Make and Model."
                                return@Button
                            }
                            if (regNumber.isBlank()) {
                                errorMsg = "Please enter registration number."
                                return@Button
                            }
                            val rate = dailyRate.toDoubleOrNull()
                            if (rate == null || rate <= 0) {
                                errorMsg = "Please enter a valid rental rate in OMR."
                                return@Button
                            }

                            val updatedEntity = VehicleEntity(
                                id = vehicle?.id ?: 0,
                                make = make.trim(),
                                model = model.trim(),
                                regNumber = regNumber.trim(),
                                category = category,
                                modelYear = modelYear.toIntOrNull() ?: 2024,
                                dailyRate = rate,
                                status = status,
                                imageDrawableName = vehicle?.imageDrawableName?.takeIf { it.isNotBlank() } ?: defaultPhoto,
                                transmission = transmission.trim(),
                                seats = seats.toIntOrNull() ?: 5,
                                fuelType = fuelType.trim(),
                                mileageKm = vehicle?.mileageKm ?: 15000
                            )
                            onSave(updatedEntity, isNew)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_vehicle_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TravzRedPrimary,
                            contentColor = TravzWhite
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isNew) "Add Vehicle to Fleet" else "Save Changes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
