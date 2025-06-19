package com.example.expensetracker.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.components.ExpenseList
import com.example.expensetracker.components.TotalCostCard
import com.example.expensetracker.entities.Event
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun EventScreen(
    event: Event,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TotalCostCard(totalCost = event.expensesList.sumOf { it.expenseCost })
        Spacer(modifier = Modifier.height(32.dp))
        ExpenseList(
            expenseList = event.expensesList,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview(eventViewModel: EventViewModel = viewModel()) {
    EventScreen(
        event = eventViewModel.events[0],
        modifier = Modifier.fillMaxSize()
    )
}