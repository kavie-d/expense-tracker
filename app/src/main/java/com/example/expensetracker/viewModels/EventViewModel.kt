package com.example.expensetracker.viewModels

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.example.expensetracker.data.getEventList
import com.example.expensetracker.entities.Event

class EventViewModel: ViewModel() {
    private val _events = getEventList().toMutableStateList()

    val events: List<Event>
        get() = _events

    fun addEvent(event: Event) {
        _events.add(0, event)
    }

    fun getEvent(eventId: Int) =_events.find { event -> event.id == eventId }
}