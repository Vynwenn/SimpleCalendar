package com.example.simplecalendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalendarPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MonthViewFragment()
            1 -> WeekViewFragment()
            2 -> DayViewFragment()
            else -> MonthViewFragment()
        }
    }
}