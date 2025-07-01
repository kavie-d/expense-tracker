package com.example.expensetracker.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var openAddExpenseDialog by mutableStateOf(false)
    var openUpdateExpenseDialog by mutableStateOf(false)
    var openDeleteExpenseDialog by mutableStateOf(false)

    var newExpenseName by mutableStateOf("")
    var newExpenseCost by mutableStateOf("")

    private var _selectedExpense = mutableStateOf<Expense?>(null)
    val selectedExpense: Expense? get() = _selectedExpense.value

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

    private fun addExpense(eventId: Int, expenseName: String, expenseCost: Int) = viewModelScope.launch {
        expensesRepository.insert(Expense(eventId = eventId, expenseName = expenseName, expenseCost = expenseCost))
    }

    private fun updateExpense(expense: Expense) = viewModelScope.launch {
        expensesRepository.update(expense)
    }

    private fun deleteExpense(expense: Expense) = viewModelScope.launch {
        expensesRepository.delete(expense)
    }

    fun onAddExpenseDialogDismiss() {
        openAddExpenseDialog = false
        newExpenseName = ""
        newExpenseCost = ""
    }

    fun onAddExpenseDialogConfirm(eventId: Int) {
        addExpense(
            eventId = eventId,
            expenseName = newExpenseName,
            expenseCost = newExpenseCost.toInt()
        )
        openAddExpenseDialog = false
        newExpenseName = ""
        newExpenseCost = ""
    }

    fun setSelectedExpense(expense: Expense) {
        _selectedExpense.value = expense
    }

    fun onSelectedExpenseNameChange(name: String) {
        _selectedExpense.value = _selectedExpense.value?.copy(expenseName = name)
    }

    fun onSelectedExpenseCostChange(cost: String) {
        _selectedExpense.value = _selectedExpense.value?.copy(expenseCost = cost.toInt())
    }

    fun onUpdateExpenseDialogDismiss() {
        openUpdateExpenseDialog = false
        _selectedExpense.value = null
    }

    fun onUpdateExpenseDialogConfirm() {
        selectedExpense?.let { updateExpense(it) }
        openUpdateExpenseDialog = false
        _selectedExpense.value = null
    }

    fun onDeleteExpenseDialogDismiss() {
        openDeleteExpenseDialog = false
        _selectedExpense.value = null
    }

    fun onDeleteExpenseDialogConfirm() {
        selectedExpense?.let { deleteExpense(it) }
        openDeleteExpenseDialog = false
        _selectedExpense.value = null
    }

}