package com.example.eventplanner.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    fun insertEvent(event: Event) = viewModelScope.launch { repository.insert(event) }

    fun updateEvent(event: Event) = viewModelScope.launch { repository.update(event) }

    fun deleteEvent(event: Event) = viewModelScope.launch { repository.delete(event) }

    fun getEventsByDate(date: String): LiveData<List<Event>> =
        repository.getEventsByDate(date.toString())

    fun getUpcomingEvents(today: String) = repository.getUpcomingEvents(today)


    fun getEventById(id: Int): LiveData<Event> = repository.getEventById(id)

    fun getAllEvents(): LiveData<List<Event>> = repository.getAllEvents()
}
