package com.example.expensetracker.data

import com.example.expensetracker.entities.Event
import com.example.expensetracker.entities.Expense

fun getEventList() = List(20) { i -> Event(i, "Event ${i + 1}", getExpenseList()) }

fun getExpenseList() = List(7) { i -> Expense(i, "Expense ${i + 1}", 450)}