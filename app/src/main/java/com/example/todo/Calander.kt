package com.example.todo;

import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity

class Calendar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calander)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Handle date selection here
            val selectedDate = "$dayOfMonth ${getMonthName(month)}, $year"
            // You can display the selected date or perform any other action here
        }

        confirmButton.setOnClickListener {
            // You can get the selected date from the CalendarView and perform further actions
            val selectedDateMillis = calendarView.date
            // Convert millis to date format as needed
        }
    }

    // Function to get month name from month index
    private fun getMonthName(month: Int): String {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
        )
        return months[month]
    }
}
