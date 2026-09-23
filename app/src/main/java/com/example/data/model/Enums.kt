package com.example.data.model

enum class VehicleCategory(val displayName: String) {
    ECONOMY("Economy"),
    SEDAN("Sedan"),
    SUV("SUV"),
    FOUR_BY_FOUR("4x4");

    companion object {
        fun fromString(value: String): VehicleCategory {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
                ?: SEDAN
        }
    }
}

enum class VehicleStatus(val label: String) {
    AVAILABLE("Available"),
    BOOKED("Booked"),
    MAINTENANCE("Maintenance"),
    SOLD("Sold"),
    IDLE("Idle");

    companion object {
        fun fromString(value: String): VehicleStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
                ?: AVAILABLE
        }
    }
}

enum class BookingStatus(val label: String) {
    CONFIRMED("Confirmed"),
    ACTIVE("Active"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    companion object {
        fun fromString(value: String): BookingStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
                ?: CONFIRMED
        }
    }
}

enum class PaymentStatus(val label: String) {
    PAID("Paid"),
    PARTIAL("Partial"),
    UNPAID("Unpaid");

    companion object {
        fun fromString(value: String): PaymentStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.label.equals(value, ignoreCase = true) }
                ?: UNPAID
        }
    }
}

enum class StaffRole(val title: String, val description: String) {
    ADMIN("Admin", "Full access: Manage fleet, cancel bookings, override rates & view reports"),
    MANAGER("Manager", "Management access: Update vehicle status, maintenance & edit bookings"),
    STAFF("Staff", "Front desk: Check availability & create new customer bookings");

    fun canManageFleet(): Boolean = this == ADMIN || this == MANAGER
    fun canDeleteOrCancel(): Boolean = this == ADMIN || this == MANAGER
    fun canEditRates(): Boolean = this == ADMIN
}
