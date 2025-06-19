package com.example.expensetracker.entities

data class Expense(
    val id: Int,
    var expenseName: String,
    var expenseCost: Int = 0
)
