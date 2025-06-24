package com.example.expensetracker.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.expensetracker.entities.EventWithExpenses
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Transaction
    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventWithExpenses(eventId: Int): Flow<EventWithExpenses>

    @Query("SELECT SUM(expense_cost) FROM expenses WHERE event_id = :eventId")
    fun getTotalCost(eventId: Int): Flow<Int?>

}