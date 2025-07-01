package com.example.expensetracker.repositories

import com.example.expensetracker.entities.EventWithExpenses
import com.example.expensetracker.entities.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getEventWithExpenses(eventId: Int): Flow<EventWithExpenses?>

    suspend fun insert(expense: Expense)

    suspend fun update(expense: Expense)

    suspend fun delete(expense: Expense)

}