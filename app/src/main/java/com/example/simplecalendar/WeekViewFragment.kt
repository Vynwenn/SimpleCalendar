package com.example.simplecalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeekViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_week_view, container, false)

        // 这里可以添加周视图的具体实现
        val textView = view.findViewById<TextView>(R.id.weekTextView)
        textView.text = "周视图 - 开发中..."

        return view
    }
}