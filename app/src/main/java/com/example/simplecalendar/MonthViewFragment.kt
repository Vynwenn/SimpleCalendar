package com.example.simplecalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MonthViewFragment : Fragment() {

    private lateinit var calendarView: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById(R.id.calendarView)

        // 设置示例事件（你可以从 Activity 传递数据过来）
        setExampleEvents()
    }

    private fun setExampleEvents() {
        val events = mutableListOf<CalendarEvent>()
        val calendar = java.util.Calendar.getInstance()

        // 添加示例事件（与原来相同的代码）
        events.add(CalendarEvent(
            id = 1,
            title = "团队会议",
            description = "每周团队同步会议",
            startTime = java.util.Date(),
            endTime = java.util.Date(calendar.timeInMillis + 3600000),
            isAllDay = false
        ))

        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)
        events.add(CalendarEvent(
            id = 2,
            title = "医生预约",
            description = "年度体检",
            startTime = calendar.time,
            endTime = java.util.Date(calendar.timeInMillis + 3600000),
            isAllDay = false
        ))

        calendar.add(java.util.Calendar.DAY_OF_MONTH, 2)
        events.add(CalendarEvent(
            id = 3,
            title = "公共假期",
            description = "国庆节",
            startTime = calendar.time,
            endTime = calendar.time,
            isAllDay = true
        ))

        calendarView.setEvents(events)
    }
}