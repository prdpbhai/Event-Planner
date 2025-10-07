package com.example.eventplanner.data

import androidx.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(private val dao: EventDao) {

    fun getAllEvents(): LiveData<List<Event>> = dao.getAllEvents()

    fun getEventsByDate(date: String): LiveData<List<Event>> = dao.getEventsByDate(date)

    fun getUpcomingEvents(today: String): LiveData<List<Event>> =
        dao.getUpcomingEvents(today)



    fun getEventById(id: Int): LiveData<Event> = dao.getEventById(id)

    suspend fun insert(event: Event): Long = dao.insert(event)

    suspend fun update(event: Event) = dao.update(event)

    suspend fun delete(event: Event) = dao.delete(event)

    suspend fun getById(id: Int): Event? = dao.getById(id)
}
