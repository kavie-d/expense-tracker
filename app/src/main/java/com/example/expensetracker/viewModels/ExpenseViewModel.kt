package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entities.Event
import com.example.expensetracker.entities.EventWithExpenses
import com.example.expensetracker.entities.Expense
import com.example.expensetracker.repositories.EventsRepository
import com.example.expensetracker.repositories.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ExpenseViewModel(
    private val expensesRepository: ExpenseRepository,
    private val eventsRepository: EventsRepository
): ViewModel() {

    private val _eventWithExpenses = MutableStateFlow<EventWithExpenses?>(null)
    val eventWithExpenses: StateFlow<EventWithExpenses?> get() = _eventWithExpenses

    private val _totalCost = MutableStateFlow(0)
    val totalCost: StateFlow<Int> get() = _totalCost

    fun loadData(eventId: Int) {
        viewModelScope.launch {
            expensesRepository.getEventWithExpenses(eventId)
                .collect { _eventWithExpenses.value = it }
        }
        viewModelScope.launch {
            eventsRepository.getTotalCost(eventId)
                .map { it ?: 0 }
                .collect { _totalCost.value = it }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    fun addExpense(eventId: Int, expenseName: String, expenseCost: Int) = viewModelScope.launch {
        expensesRepository.insert(Expense(eventId = eventId, expenseName = expenseName, expenseCost = expenseCost))
    }

    fun updateEvent(event: Event) = viewModelScope.launch {
        eventsRepository.update(event)
    }

    fun deleteEvent(event: Event) = viewModelScope.launch {
        eventsRepository.delete(event)
    }

}