package com.example.expensetracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.expensetracker.entities.EventWithExpenses
import com.example.expensetracker.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Transaction
    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventWithExpenses(eventId: Int): Flow<EventWithExpenses?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

}