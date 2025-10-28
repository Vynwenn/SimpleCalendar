package com.example.simplecalendar

import java.util.Calendar
import java.util.Date

data class CalendarEvent(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: Date,
    val endTime: Date,
    val isAllDay: Boolean = false,
    val location: String? = null
) {
    // 添加 isSameDay 方法
    fun isSameDay(otherDate: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = startTime

        val cal2 = Calendar.getInstance()
        cal2.time = otherDate

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}