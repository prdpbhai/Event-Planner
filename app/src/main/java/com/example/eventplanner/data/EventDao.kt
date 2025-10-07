package com.example.eventplanner.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY date ASC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE date = :date ORDER BY time")
    fun getEventsByDate(date: String): LiveData<List<Event>>

    @Query("SELECT * FROM events WHERE date >= :today ORDER BY date, time")
    fun getUpcomingEvents(today: String): LiveData<List<Event>>


    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): LiveData<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Event?
}
