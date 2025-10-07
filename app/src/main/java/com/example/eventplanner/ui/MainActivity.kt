package com.example.eventplanner.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventplanner.R
import com.example.eventplanner.databinding.ActivityMainBinding
import com.example.eventplanner.data.EventAdapter
import com.example.eventplanner.data.EventViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var todayDate = ""
    private var isFabMenuOpen = false

    private lateinit var fabMain: FloatingActionButton
    private lateinit var fabAddEvent: FloatingActionButton
    private lateinit var fabUpcomingEvents: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Change status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)
        }
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))




        fabMain = binding.fabMain
        fabAddEvent = binding.fabAddEvent
        fabUpcomingEvents = binding.fabUpcomingEvents

        setupCalendar()
        setupRecyclerView()
        setupFab()
    }


    private fun setupRecyclerView() {
        adapter = EventAdapter(
            onEditClick = { event ->
                val intent = Intent(this, AddEditEventActivity::class.java)
                intent.putExtra("event_id", event.id)
                startActivity(intent)
            },
            onDeleteClick = { event ->
                viewModel.deleteEvent(event)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }


    private fun loadEventsForDate(dateString: String) {
        viewModel.getEventsByDate(dateString).observe(this) { events ->
            adapter.submitList(events ?: emptyList())
        }
    }


    private fun toggleFabMenu() {
        if (isFabMenuOpen) {
            // Hide menu options
            binding.layoutAddEvent.visibility = View.GONE
            binding.layoutUpcoming.visibility = View.GONE
            fabMain.animate().rotation(0f).setDuration(200).start()
        } else {
            // Show menu options
            binding.layoutAddEvent.visibility = View.VISIBLE
            binding.layoutUpcoming.visibility = View.VISIBLE
            fabMain.animate().rotation(45f).setDuration(200).start() // Rotate main FAB for animation
        }
        isFabMenuOpen = !isFabMenuOpen
    }


    private fun setupFab() {
        fabMain.setOnClickListener {
            toggleFabMenu()
        }

        fabAddEvent.setOnClickListener {
            toggleFabMenu()
            startActivity(Intent(this, AddEditEventActivity::class.java))
        }

        fabUpcomingEvents.setOnClickListener {
            toggleFabMenu()
            Toast.makeText(this, "Upcoming Events clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,UpcomingEventsActivity::class.java))
            // TODO: Navigate to UpcomingEventsActivity
        }
    }


    private fun setupCalendar() {
        val today = Calendar.getInstance()
        todayDate = sdf.format(today.time)
        binding.tvSelectedDate.text = todayDate
        Log.d("EventPlanner", "Today: $todayDate")

        // Load today's events
        loadEventsForDate(todayDate)

        // Listen for date change
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val selectedDateString = sdf.format(calendar.time)
            binding.tvSelectedDate.text = selectedDateString
            loadEventsForDate(selectedDateString)
        }
    }
}
