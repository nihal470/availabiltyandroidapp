package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtils {
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.US)

    // Current default reference date (aligned with prompt timestamp)
    val TODAY_ISO: String = "2026-09-23"

    fun today(): String = TODAY_ISO

    fun formatDisplay(isoDate: String): String {
        return try {
            val date = isoFormat.parse(isoDate) ?: return isoDate
            displayFormat.format(date)
        } catch (_: Exception) {
            isoDate
        }
    }

    fun formatMonthYear(year: Int, month: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return monthYearFormat.format(cal.time)
    }

    fun calculateDays(pickupDate: String, dropoffDate: String): Int {
        return try {
            val start = isoFormat.parse(pickupDate) ?: return 1
            val end = isoFormat.parse(dropoffDate) ?: return 1
            val diffMs = end.time - start.time
            val days = TimeUnit.MILLISECONDS.toDays(diffMs).toInt()
            if (days <= 0) 1 else days
        } catch (_: Exception) {
            1
        }
    }

    fun addDays(isoDate: String, daysToAdd: Int): String {
        return try {
            val cal = Calendar.getInstance()
            val date = isoFormat.parse(isoDate) ?: Date()
            cal.time = date
            cal.add(Calendar.DAY_OF_MONTH, daysToAdd)
            isoFormat.format(cal.time)
        } catch (_: Exception) {
            isoDate
        }
    }

    /**
     * Checks if two date intervals [startA, endA] and [startB, endB] overlap.
     * Inclusive of both start and end days.
     */
    fun doDateRangesOverlap(startA: String, endA: String, startB: String, endB: String): Boolean {
        // String comparison works accurately with ISO "yyyy-MM-dd"
        return startA <= endB && endA >= startB
    }

    /**
     * Checks if a single date falls within the range [startDate, endDate]
     */
    fun isDateInRange(targetDate: String, startDate: String, endDate: String): Boolean {
        return targetDate in startDate..endDate
    }

    fun formatOmr(amount: Double): String {
        return String.format(Locale.US, "%.3f OMR", amount)
    }
}
