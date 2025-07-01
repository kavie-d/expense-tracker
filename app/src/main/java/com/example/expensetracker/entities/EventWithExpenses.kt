package com.example.expensetracker.entities

import androidx.room.Embedded
import androidx.room.Relation

data class EventWithExpenses(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "id",
        entityColumn = "event_id"
    )
    val expenses: List<Expense>
)