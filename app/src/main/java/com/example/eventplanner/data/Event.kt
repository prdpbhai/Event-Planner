package com.example.eventplanner.data



import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var description: String,
// store date as epoch day (midnight) or ISO string -- we'll store as yyyy-MM-dd
    var date: String,
    var time: String // store as HH:mm
)