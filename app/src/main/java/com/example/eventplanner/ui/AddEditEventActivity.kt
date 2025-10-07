package com.example.eventplanner.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.eventplanner.R
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventViewModel
import com.example.eventplanner.databinding.ActivityAddEditEventBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditEventBinding
    private val viewModel: EventViewModel by viewModels()

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTime: Calendar = Calendar.getInstance()
    private var eventId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        // Check if we are editing an existing event
        eventId = intent.getIntExtra("event_id", -1).takeIf { it != -1 }

        Log.d("jdhiud",eventId.toString())

        if (eventId != null) {
            viewModel.getEventById(eventId!!).observe(this) { event ->
                event?.let { populateFields(it) }
            }
        }

        binding.etDate.setOnClickListener { showDatePicker() }
        binding.etTime.setOnClickListener { showTimePicker() }

        binding.btnSave.setOnClickListener {
            saveEvent()
        }
    }

    private fun populateFields(event: Event) {
        binding.etTitle.setText(event.title)
        binding.etDescription.setText(event.description)
        binding.etDate.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time))
        binding.etTime.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.time))
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                binding.etDate.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                binding.etTime.setText(SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveEvent() {
        val title = binding.etTitle.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.etDate.text.isNullOrEmpty() || binding.etTime.text.isNullOrEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show()
            return
        }


        val calendar = Calendar.getInstance()
        calendar.set(
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH),
            selectedTime.get(Calendar.HOUR_OF_DAY),
            selectedTime.get(Calendar.MINUTE)
        )
        val timestamp = calendar.timeInMillis

        var date= SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
        var time=SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.time)

        val event = Event(
            id = eventId ?: 0,
            title = title,
            description = description,
            date = date,
            time = time
        )

        Log.d("rdcfvgbhnjhgyftdrse","${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)}")


        if (eventId == null) {
            viewModel.insertEvent(event)
            Toast.makeText(this, "Event added", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.updateEvent(event)
            Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}
