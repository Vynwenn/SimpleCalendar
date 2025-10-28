package com.example.simplecalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DayViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day_view, container, false)

        // 这里可以添加日视图的具体实现
        val textView = view.findViewById<TextView>(R.id.dayTextView)
        textView.text = "日视图 - 开发中..."

        return view
    }
}