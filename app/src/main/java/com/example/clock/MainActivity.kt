package com.example.clock

import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TimePicker
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var btSetTime: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clockView: ClockView = findViewById(R.id.clockView)
        btSetTime = findViewById(R.id.bt_set_time)
        clockView.calendar = Calendar.getInstance()
//        clockView.calendar.add(Calendar.HOUR, 1)

        btSetTime.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val gio: Int = calendar.get(Calendar.HOUR_OF_DAY)
            val phut: Int = calendar.get(Calendar.MINUTE)
            val timePickerDialog: TimePickerDialog = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    calendar.set(Calendar.HOUR_OF_DAY, p1)
                    calendar.set(Calendar.MINUTE, p2)
                    clockView.calendar = calendar
                }
            }, gio, phut, true)
            timePickerDialog.show()
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
//                calendar.set(Calendar.HOUR_OF_DAY, i)
//                calendar.set(Calendar.MINUTE, i2)
//                clockView.calendar = calendar
//            }
//            TimePickerDialog(this, timeSetListener, gio, phut, true).show()
        }
    }
}