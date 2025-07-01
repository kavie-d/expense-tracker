package com.example.expensetracker.repositories

import com.example.expensetracker.entities.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {

    suspend fun insert(event: Event)

    suspend fun update(event: Event)

    suspend fun delete(event: Event)

    fun getEvent(eventId: Int): Flow<Event>

    fun getAllEvents(): Flow<List<Event>>

    fun getTotalCost(eventId: Int): Flow<Int?>

}