package com.example.expensetracker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("event_name") var eventName: String,
    @ColumnInfo("created_at") val createdAt: Date = Date()
) {

    fun getFormattedDate(): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(createdAt)
    }

}