package com.example.simplecalendar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter(
    private val context: Context,
    private val dates: List<Date?>,
    private val events: List<CalendarEvent> = emptyList()
) : BaseAdapter() {

    override fun getCount(): Int = dates.size
    override fun getItem(position: Int): Any = dates[position] ?: ""
    override fun getItemId(position: Int): Long = position.toLong()

    // 新增方法：获取指定位置的日期
    fun getItemAt(position: Int): Date? = dates[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.calendar_day_item, parent, false)

        val date = dates[position]
        val dayText = view.findViewById<TextView>(R.id.dayText)
        val eventIndicator = view.findViewById<View>(R.id.eventIndicator)

        if (date == null) {
            dayText.text = ""
            eventIndicator.visibility = View.GONE
            return view
        }

        val calendar = Calendar.getInstance().apply { time = date }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        dayText.text = day.toString()

        // 检查是否是今天
        val today = Calendar.getInstance()
        val isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)

        // 检查是否是当前月（对于月视图）
        val currentMonthCalendar = Calendar.getInstance()
        val isCurrentMonth = calendar.get(Calendar.MONTH) == currentMonthCalendar.get(Calendar.MONTH) &&
                calendar.get(Calendar.YEAR) == currentMonthCalendar.get(Calendar.YEAR)

        // 设置样式
        if (isToday) {
            dayText.setBackgroundColor(Color.RED)
            dayText.setTextColor(Color.WHITE)
        } else if (isCurrentMonth) {
            dayText.setBackgroundColor(Color.TRANSPARENT)
            dayText.setTextColor(Color.BLACK)
        } else {
            dayText.setBackgroundColor(Color.TRANSPARENT)
            dayText.setTextColor(Color.GRAY)
        }

        // 显示事件指示器
        val hasEvents = events.any { event ->
            val eventCalendar = Calendar.getInstance().apply { time = event.startTime }
            eventCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    eventCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    eventCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
        }
        eventIndicator.visibility = if (hasEvents) View.VISIBLE else View.GONE

        return view
    }
}