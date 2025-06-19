package com.example.expensetracker.entities

import java.util.Date

data class Event(
    val id: Int,
    var eventName: String,
    var totalCost: Int = 0,
    var expensesList: List<Expense> = listOf(),
    val createdAt: Date = Date()
)
