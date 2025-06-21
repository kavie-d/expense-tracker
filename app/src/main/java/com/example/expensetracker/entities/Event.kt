package com.example.expensetracker.entities

import java.util.Date

data class Event(
    val id: Int,
    val eventName: String,
    val expensesList: List<Expense> = listOf(),
    val createdAt: Date = Date()
) {
    val totalCost: Int
        get() = expensesList.sumOf { it.expenseCost }
}
