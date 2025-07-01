package com.example.expensetracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEvent(eventId: Int): Flow<Event>

    @Query("SELECT * FROM events ORDER BY created_at DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT SUM(expense_cost) FROM expenses WHERE event_id = :eventId")
    fun getTotalCost(eventId: Int): Flow<Int?>
}