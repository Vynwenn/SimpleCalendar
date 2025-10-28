package com.example.simplecalendar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var currentCalendar: Calendar = Calendar.getInstance()
    private var events: List<CalendarEvent> = emptyList()
    private lateinit var monthYearText: TextView
    private lateinit var calendarGrid: GridView
    private lateinit var daysOfWeekGrid: GridView

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        orientation = VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.calendar_layout, this, true)

        monthYearText = findViewById(R.id.monthYearText)
        calendarGrid = findViewById(R.id.calendarGrid)
        daysOfWeekGrid = findViewById(R.id.daysOfWeekGrid)

        setupDaysOfWeek()
        updateCalendar()

        // 设置月份导航
        findViewById<TextView>(R.id.prevMonthBtn).setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        findViewById<TextView>(R.id.nextMonthBtn).setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        findViewById<TextView>(R.id.todayBtn).setOnClickListener {
            currentCalendar = Calendar.getInstance()
            updateCalendar()
        }
    }

    private fun setupDaysOfWeek() {
        val daysOfWeek = arrayOf("日", "一", "二", "三", "四", "五", "六")
        daysOfWeekGrid.adapter = object : BaseAdapter() {
            override fun getCount(): Int = daysOfWeek.size
            override fun getItem(position: Int): Any = daysOfWeek[position]
            override fun getItemId(position: Int): Long = position.toLong()
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val textView = if (convertView == null) {
                    TextView(context).apply {
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        setTextColor(Color.DKGRAY)
                        setBackgroundColor(Color.LTGRAY)
                        setPadding(8, 16, 8, 16)
                    }
                } else {
                    convertView as TextView
                }
                textView.text = daysOfWeek[position]
                return textView
            }
        }
    }

    private fun updateCalendar() {
        updateMonthYearText()
        updateCalendarGrid()
    }

    private fun updateMonthYearText() {
        val dateFormat = SimpleDateFormat("yyyy年 MM月", Locale.getDefault())
        monthYearText.text = dateFormat.format(currentCalendar.time)
    }

    private fun updateCalendarGrid() {
        val calendar = currentCalendar.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val totalCells = 42 // 6行 x 7列
        val calendarDays = mutableListOf<Date?>()

        // 添加上个月的日期
        val prevMonthDays = firstDayOfMonth - 1
        val prevMonthCalendar = calendar.clone() as Calendar
        prevMonthCalendar.add(Calendar.MONTH, -1)
        val daysInPrevMonth = prevMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in prevMonthDays downTo 1) {
            prevMonthCalendar.set(Calendar.DAY_OF_MONTH, daysInPrevMonth - i + 1)
            calendarDays.add(prevMonthCalendar.time)
        }

        // 添加当前月的日期
        for (day in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendarDays.add(calendar.time)
        }

        // 添加下个月的日期
        val remainingCells = totalCells - calendarDays.size
        val nextMonthCalendar = calendar.clone() as Calendar
        nextMonthCalendar.add(Calendar.MONTH, 1)

        for (day in 1..remainingCells) {
            nextMonthCalendar.set(Calendar.DAY_OF_MONTH, day)
            calendarDays.add(nextMonthCalendar.time)
        }

        calendarGrid.adapter = CalendarAdapter(calendarDays, events)

        calendarGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val date = calendarDays[position]
            date?.let {
                showDateEvents(it)
            }
        }
    }

    fun setEvents(newEvents: List<CalendarEvent>) {
        events = newEvents
        updateCalendarGrid()
    }

    private fun showDateEvents(date: Date) {
        val dateEvents = events.filter { it.isSameDay(date) }
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
        val message = if (dateEvents.isNotEmpty()) {
            "${dateFormat.format(date)}的事件:\n" +
                    dateEvents.joinToString("\n") { "• ${it.title}" }
        } else {
            "${dateFormat.format(date)} 没有事件"
        }

        android.app.AlertDialog.Builder(context)
            .setTitle("事件")
            .setMessage(message)
            .setPositiveButton("确定", null)
            .show()
    }

    private inner class CalendarAdapter(
        private val dates: List<Date?>,
        private val events: List<CalendarEvent>
    ) : BaseAdapter() {

        override fun getCount(): Int = dates.size
        override fun getItem(position: Int): Any = dates[position] ?: ""
        override fun getItemId(position: Int): Long = position.toLong()

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

            // 检查是否是当前月
            val isCurrentMonth = calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)

            // 设置样式 - 使用Color常量
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
            val hasEvents = events.any { it.isSameDay(date) }
            eventIndicator.visibility = if (hasEvents) View.VISIBLE else View.GONE

            return view
        }
    }
}