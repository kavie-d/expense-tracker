package com.example.expensetracker.config

import android.content.Context
import com.example.expensetracker.repositories.EventsRepository
import com.example.expensetracker.repositories.ExpenseRepository
import com.example.expensetracker.repositories.OfflineEventsRepository
import com.example.expensetracker.repositories.OfflineExpenseRepository
import com.example.expensetracker.utils.AppDatabase

interface AppContainer {
    val eventsRepository: EventsRepository
    val expenseRepository: ExpenseRepository
}

class AppDataContainer(private val context: Context): AppContainer {

    override val eventsRepository: EventsRepository by lazy {
        OfflineEventsRepository(AppDatabase.getDatabase(context).eventDao())
    }

    override val expenseRepository: ExpenseRepository by lazy {
        OfflineExpenseRepository(AppDatabase.getDatabase(context).expenseDao())
    }

}