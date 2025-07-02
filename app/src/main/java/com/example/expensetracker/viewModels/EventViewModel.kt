package com.example.expensetracker.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.entities.Event
import com.example.expensetracker.repositories.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EventViewModel(private val eventsRepository: EventsRepository): ViewModel() {

    var openAddEventDialog by mutableStateOf(false)
    var openUpdateEventDialog by mutableStateOf(false)
    var openDeleteEventDialog by mutableStateOf(false)

    var newEventName by mutableStateOf("")

    private var _selectedEvent = mutableStateOf<Event?>(null)
    val selectedEvent: Event? get() = _selectedEvent.value

    var isAddEventNameError by mutableStateOf(false)
    var isUpdateEventNameError by mutableStateOf(false)

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val events: StateFlow<List<Event>> = eventsRepository.getAllEvents()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    fun getTotalCost(eventId: Int): StateFlow<Int> = eventsRepository.getTotalCost(eventId)
        .map { it ?: 0 }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = 0
        )

    private fun addEvent(eventName: String) = viewModelScope.launch {
        eventsRepository.insert(Event(eventName = eventName))
    }

    private fun updateEvent(event: Event) = viewModelScope.launch {
        eventsRepository.update(event)
    }

    private fun deleteEvent(event: Event) = viewModelScope.launch {
        eventsRepository.delete(event)
    }

    fun onAddEventDialogDismiss() {
        openAddEventDialog = false
        newEventName = ""
        isAddEventNameError = false
    }

    fun onAddEventDialogConfirm() {
        isAddEventNameError = false

        if (!validateEventName(newEventName)) {
            isAddEventNameError = true
            return
        }

        addEvent(newEventName)
        openAddEventDialog = false
        newEventName = ""
    }

    fun setSelectedEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun onSelectedEventNameChange(name: String) {
        _selectedEvent.value = _selectedEvent.value?.copy(eventName = name)
    }

    fun onUpdateEventDialogDismiss() {
        openUpdateEventDialog = false
        _selectedEvent.value = null
        isUpdateEventNameError = false
    }

    fun onUpdateEventDialogConfirm() {
        isUpdateEventNameError = false

        val tempEvent = selectedEvent
        if (tempEvent != null) {
            if (!validateEventName(tempEvent.eventName)) {
                isUpdateEventNameError = true
                return
            }

            updateEvent(tempEvent)
        }

        openUpdateEventDialog = false
        _selectedEvent.value = null
    }

    fun onDeleteEventDialogDismiss() {
        openDeleteEventDialog = false
        _selectedEvent.value = null
    }

    fun onDeleteEventDialogConfirm() {
        selectedEvent?.let { deleteEvent(it) }
        openDeleteEventDialog = false
        _selectedEvent.value = null
    }

    private fun validateEventName(eventName: String): Boolean {
        return eventName.isNotEmpty()
    }
}