package com.example.expensetracker.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("event_id") val eventId: Int = 0,
    @ColumnInfo("expense_name") var expenseName: String,
    @ColumnInfo("expense_cost") var expenseCost: Int = 0
)