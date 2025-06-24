package com.example.expensetracker.repositories

import com.example.expensetracker.dao.ExpensesDao
import com.example.expensetracker.entities.EventWithExpenses
import kotlinx.coroutines.flow.Flow

class OfflineExpenseRepository(private val expenseDao: ExpensesDao): ExpenseRepository {

    override fun getEventWithExpenses(eventId: Int): Flow<EventWithExpenses> = expenseDao.getEventWithExpenses(eventId)

    override fun getTotalCost(eventId: Int): Flow<Int?> = expenseDao.getTotalCost(eventId)

}