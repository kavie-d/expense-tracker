package com.example.expensetracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.entities.Expense
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun ExpenseList(
    expenseList: List<Expense>,
    modifier: Modifier = Modifier
) {
    if (expenseList.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(expenseList) { expense ->
                ExpenseItem(expense = expense)
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(text = "No Expenses Added.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseListPreview(eventViewModel: EventViewModel = viewModel()) {
    ExpenseList(expenseList = getExpenseList())
}

private fun getExpenseList() = List(8) { i -> Expense(i, "Expense ${i + 1}", 450)}