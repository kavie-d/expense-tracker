package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entities.Event
import com.example.expensetracker.entities.EventWithExpenses
import com.example.expensetracker.repositories.ExpenseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ExpenseViewModel(private val expensesRepository: ExpenseRepository): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun getExpenses(eventId: Int): StateFlow<EventWithExpenses> = expensesRepository.getEventWithExpenses(eventId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = EventWithExpenses(
                event = Event(eventName = ""),
                expenses = emptyList()
            )
        )

    fun getTotalCost(eventId: Int): StateFlow<Int> = expensesRepository.getTotalCost(eventId)
        .map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = 0
        )

}