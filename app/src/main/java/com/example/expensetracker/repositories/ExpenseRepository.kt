package com.example.expensetracker.repositories

import com.example.expensetracker.entities.EventWithExpenses
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getEventWithExpenses(eventId: Int): Flow<EventWithExpenses>

    fun getTotalCost(eventId: Int): Flow<Int?>

}