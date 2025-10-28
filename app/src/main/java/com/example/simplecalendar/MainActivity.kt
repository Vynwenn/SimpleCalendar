package com.example.simplecalendar

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    private lateinit var monthYearText: TextView
    private lateinit var monthGrid: GridView
    private lateinit var weekGrid: GridView
    private lateinit var weekView: LinearLayout
    private lateinit var dayView: ScrollView
    private lateinit var dayEventsLayout: LinearLayout
    private lateinit var weekHeader: LinearLayout

    private var currentCalendar: Calendar = Calendar.getInstance()
    private var currentView: String = "month" // month, week, day

    // 示例事件数据
    private val events = mutableListOf<CalendarEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupSampleEvents()
        setupClickListeners()
        updateView()
    }

    private fun initializeViews() {
        monthYearText = findViewById(R.id.monthYearText)
        monthGrid = findViewById(R.id.monthGrid)
        weekGrid = findViewById(R.id.weekGrid)
        weekView = findViewById(R.id.weekView)
        dayView = findViewById(R.id.dayView)
        dayEventsLayout = findViewById(R.id.dayEventsLayout)
        weekHeader = findViewById(R.id.weekHeader)
    }

    private fun setupSampleEvents() {
        val calendar = Calendar.getInstance()

        // 今天的事件
        events.add(CalendarEvent(
            id = 1,
            title = "团队会议",
            description = "每周团队同步会议",
            startTime = calendar.time,
            endTime = Date(calendar.timeInMillis + 3600000),
            isAllDay = false
        ))

        // 明天的事件
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        events.add(CalendarEvent(
            id = 2,
            title = "医生预约",
            description = "年度体检",
            startTime = calendar.time,
            endTime = Date(calendar.timeInMillis + 3600000),
            isAllDay = false
        ))

        // 后天的事件
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        events.add(CalendarEvent(
            id = 3,
            title = "公共假期",
            description = "国庆节",
            startTime = calendar.time,
            endTime = calendar.time,
            isAllDay = true
        ))

        // 本周另一个事件
        calendar.add(Calendar.DAY_OF_MONTH, 2)
        events.add(CalendarEvent(
            id = 4,
            title = "项目评审",
            description = "季度项目评审会议",
            startTime = calendar.time,
            endTime = Date(calendar.timeInMillis + 7200000),
            isAllDay = false
        ))
    }

    private fun setupClickListeners() {
        // 导航按钮
        findViewById<TextView>(R.id.prevBtn).setOnClickListener {
            when (currentView) {
                "month" -> currentCalendar.add(Calendar.MONTH, -1)
                "week" -> currentCalendar.add(Calendar.WEEK_OF_YEAR, -1)
                "day" -> currentCalendar.add(Calendar.DAY_OF_MONTH, -1)
            }
            updateView()
        }

        findViewById<TextView>(R.id.nextBtn).setOnClickListener {
            when (currentView) {
                "month" -> currentCalendar.add(Calendar.MONTH, 1)
                "week" -> currentCalendar.add(Calendar.WEEK_OF_YEAR, 1)
                "day" -> currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            updateView()
        }

        findViewById<Button>(R.id.todayBtn).setOnClickListener {
            currentCalendar = Calendar.getInstance()
            updateView()
        }

        // 视图切换按钮
        findViewById<Button>(R.id.monthViewBtn).setOnClickListener {
            switchView("month")
        }

        findViewById<Button>(R.id.weekViewBtn).setOnClickListener {
            switchView("week")
        }

        findViewById<Button>(R.id.dayViewBtn).setOnClickListener {
            switchView("day")
        }

        // 月视图日期点击
        monthGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val date = (monthGrid.adapter as CalendarAdapter).getItemAt(position)
            date?.let {
                currentCalendar.time = it
                switchView("day")
            }
        }

        // 周视图日期点击
        weekGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val date = (weekGrid.adapter as CalendarAdapter).getItemAt(position)
            date?.let {
                currentCalendar.time = it
                switchView("day")
            }
        }
    }

    private fun switchView(view: String) {
        currentView = view
        updateViewButtons()
        updateView()
    }

    private fun updateViewButtons() {
        val monthBtn = findViewById<Button>(R.id.monthViewBtn)
        val weekBtn = findViewById<Button>(R.id.weekViewBtn)
        val dayBtn = findViewById<Button>(R.id.dayViewBtn)

        // 重置所有按钮颜色
        monthBtn.setBackgroundColor(Color.parseColor("#64B5F6"))
        weekBtn.setBackgroundColor(Color.parseColor("#64B5F6"))
        dayBtn.setBackgroundColor(Color.parseColor("#64B5F6"))

        // 设置当前视图按钮为高亮
        when (currentView) {
            "month" -> monthBtn.setBackgroundColor(Color.parseColor("#1976D2"))
            "week" -> weekBtn.setBackgroundColor(Color.parseColor("#1976D2"))
            "day" -> dayBtn.setBackgroundColor(Color.parseColor("#1976D2"))
        }
    }

    private fun updateView() {
        updateHeaderText()

        when (currentView) {
            "month" -> showMonthView()
            "week" -> showWeekView()
            "day" -> showDayView()
        }
    }

    private fun updateHeaderText() {
        val headerText = when (currentView) {
            "month" -> {
                val dateFormat = SimpleDateFormat("yyyy年 MM月", Locale.getDefault())
                dateFormat.format(currentCalendar.time)
            }
            "week" -> {
                val dateFormat = SimpleDateFormat("yyyy年 MM月", Locale.getDefault())
                val weekNum = currentCalendar.get(Calendar.WEEK_OF_YEAR)
                "${dateFormat.format(currentCalendar.time)} 第${weekNum}周"
            }
            "day" -> {
                val dateFormat = SimpleDateFormat("yyyy年 MM月 dd日 EEEE", Locale.getDefault())
                dateFormat.format(currentCalendar.time)
            }
            else -> {
                val dateFormat = SimpleDateFormat("yyyy年 MM月", Locale.getDefault())
                dateFormat.format(currentCalendar.time)
            }
        }
        monthYearText.text = headerText
    }

    private fun showMonthView() {
        monthGrid.visibility = View.VISIBLE
        weekView.visibility = View.GONE
        dayView.visibility = View.GONE
        weekHeader.visibility = View.VISIBLE

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

        monthGrid.adapter = CalendarAdapter(this, calendarDays, events)
    }

    private fun showWeekView() {
        monthGrid.visibility = View.GONE
        weekView.visibility = View.VISIBLE
        dayView.visibility = View.GONE
        weekHeader.visibility = View.VISIBLE

        val calendar = currentCalendar.clone() as Calendar
        // 设置到本周的第一天（周日）
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val weekDays = mutableListOf<Date>()
        for (i in 0 until 7) {
            weekDays.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        weekGrid.adapter = CalendarAdapter(this, weekDays, events)
    }

    private fun showDayView() {
        monthGrid.visibility = View.GONE
        weekView.visibility = View.GONE
        dayView.visibility = View.VISIBLE
        weekHeader.visibility = View.GONE

        // 清空现有事件显示
        dayEventsLayout.removeAllViews()

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dayEvents = events.filter {
            Calendar.getInstance().apply { time = it.startTime }.get(Calendar.DAY_OF_YEAR) ==
                    currentCalendar.get(Calendar.DAY_OF_YEAR) &&
                    Calendar.getInstance().apply { time = it.startTime }.get(Calendar.YEAR) ==
                    currentCalendar.get(Calendar.YEAR)
        }

        if (dayEvents.isEmpty()) {
            val noEventsText = TextView(this)
            noEventsText.text = "今天没有事件"
            noEventsText.textSize = 16f
            noEventsText.setTextColor(Color.GRAY)
            noEventsText.setPadding(0, 50, 0, 0)
            noEventsText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            dayEventsLayout.addView(noEventsText)
        } else {
            for (event in dayEvents.sortedBy { it.startTime }) {
                val eventView = LinearLayout(this)
                eventView.orientation = LinearLayout.VERTICAL
                eventView.setPadding(0, 16, 0, 16)

                val titleText = TextView(this)
                titleText.text = event.title
                titleText.textSize = 18f
                titleText.setTextColor(Color.BLACK)

                val timeText = TextView(this)
                timeText.text = if (event.isAllDay) {
                    "全天"
                } else {
                    "${dateFormat.format(event.startTime)} - ${dateFormat.format(event.endTime)}"
                }
                timeText.textSize = 14f
                timeText.setTextColor(Color.GRAY)

                val descText = TextView(this)
                descText.text = event.description
                descText.textSize = 14f
                descText.setTextColor(Color.DKGRAY)

                eventView.addView(titleText)
                eventView.addView(timeText)
                eventView.addView(descText)

                // 添加分隔线
                val divider = View(this)
                divider.setBackgroundColor(Color.LTGRAY)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
                )

                dayEventsLayout.addView(eventView)
                dayEventsLayout.addView(divider)
            }
        }
    }
}