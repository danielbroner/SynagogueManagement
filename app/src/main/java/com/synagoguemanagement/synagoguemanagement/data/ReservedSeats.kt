package com.synagoguemanagement.synagoguemanagement.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ReservedSeats(var date: String, var seats: List<Seat>) {

    init {
        this.date = checkDateValidOrDefaultToToday(date)
    }

    constructor() : this("", listOf())

    fun add(newSeats: List<Seat>): ReservedSeats {
        this.seats += newSeats
        return this
    }

    private fun checkDateValidOrDefaultToToday(date: String): String {
        if (isRealDate(date)) {
            return date
        }

        return today()
    }

    private fun isRealDate(date: String): Boolean {
        val format = getFormatter()
        format.isLenient = false

        return try {
            format.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        fun dateToString(date: Date): String {
            return getFormatter().format(date)
        }

        private fun getFormatter(): SimpleDateFormat {
            return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        }

        fun today() = dateToString(Calendar.getInstance().time)
    }
}
