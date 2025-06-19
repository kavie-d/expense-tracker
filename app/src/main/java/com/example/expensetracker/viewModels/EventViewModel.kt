package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModel
import com.example.expensetracker.entities.Event
import com.example.expensetracker.entities.Expense

class EventViewModel: ViewModel() {
    private val _events = getEventList()

    val events: List<Event>
        get() = _events
}

private fun getEventList() = List(20) { i -> Event(i, "Event ${i + 1}", 2100, getExpenseList()) }

private fun getExpenseList() = List(7) { i -> Expense(i, "Expense ${i + 1}", 450)}