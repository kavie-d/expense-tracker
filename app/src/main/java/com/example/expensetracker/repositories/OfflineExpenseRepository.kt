package com.example.expensetracker.repositories

import com.example.expensetracker.dao.ExpensesDao
import com.example.expensetracker.entities.EventWithExpenses
import com.example.expensetracker.entities.Expense
import kotlinx.coroutines.flow.Flow

class OfflineExpenseRepository(private val expenseDao: ExpensesDao): ExpenseRepository {

    override fun getEventWithExpenses(eventId: Int): Flow<EventWithExpenses?> = expenseDao.getEventWithExpenses(eventId)

    override suspend fun insert(expense: Expense) = expenseDao.insert(expense)

    override suspend fun update(expense: Expense) = expenseDao.update(expense)

    override suspend fun delete(expense: Expense) = expenseDao.delete(expense)

}