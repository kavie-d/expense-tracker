package com.example.expensetracker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("event_name") val eventName: String,
    @ColumnInfo("created_at") val createdAt: Date = Date()
)