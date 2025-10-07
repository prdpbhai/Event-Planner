package com.example.eventplanner.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventplanner.R
import com.example.eventplanner.data.EventAdapter
import com.example.eventplanner.data.EventViewModel
import com.example.eventplanner.databinding.ActivityUpcomingEventsBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class UpcomingEventsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpcomingEventsBinding
    private val viewModel: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpcomingEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))

        setupRecyclerView()
        loadUpcomingEvents()
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
        binding.recyclerViewUpcoming.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewUpcoming.adapter = adapter
    }

    private fun loadUpcomingEvents() {
        val today = Calendar.getInstance().timeInMillis
        viewModel.getUpcomingEvents(today.toString()).observe(this) { events ->
            adapter.submitList(events)
        }
    }
}
