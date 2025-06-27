package com.example.expensetracker.viewModels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.expensetracker.config.InitApplicationContainer

object AppViewModelProvider {

    val Factory = viewModelFactory {

        initializer {
            EventViewModel(initAppContainer().container.eventsRepository)
        }

        initializer {
            ExpenseViewModel(
                initAppContainer().container.expenseRepository,
                initAppContainer().container.eventsRepository
            )
        }

    }

}

fun CreationExtras.initAppContainer(): InitApplicationContainer = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InitApplicationContainer)