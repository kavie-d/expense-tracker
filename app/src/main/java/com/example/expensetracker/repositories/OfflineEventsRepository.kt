package com.example.expensetracker.repositories

import com.example.expensetracker.dao.EventDao
import com.example.expensetracker.entities.Event
import kotlinx.coroutines.flow.Flow

class OfflineEventsRepository(private val eventDao: EventDao): EventsRepository {

    override suspend fun insert(event: Event) = eventDao.insert(event)

    override suspend fun update(event: Event) = eventDao.update(event)

    override suspend fun delete(event: Event) = eventDao.delete(event)

    override fun getEvent(eventId: Int): Flow<Event> = eventDao.getEvent(eventId)

    override fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    override fun getTotalCost(eventId: Int): Flow<Int?> = eventDao.getTotalCost(eventId)
}