package com.example.expensetracker.ui.screens.expenses

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.R
import com.example.expensetracker.data.getExpenseList
import com.example.expensetracker.entities.Expense
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun ExpensesScreen(
    eventId: Int,
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel = viewModel()
) {
    val event = eventViewModel.getEvent(eventId)

    Column(modifier = modifier) {
        if (event != null) {
            TotalCostCard(totalCost = event.expensesList.sumOf { it.expenseCost })
            Spacer(modifier = Modifier.height(32.dp))
            ExpenseList(
                expenseList = event.expensesList,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Invalid event.", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

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

@Composable
fun ExpenseItem(
    expense: Expense,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = expense.expenseName, style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(R.string.cost_in_rs, expense.expenseCost), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun TotalCostCard(
    modifier: Modifier = Modifier,
    totalCost: Int = 0
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(40.dp)
            )
            Column {
                Text(
                    text = stringResource(R.string.string_total),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = stringResource(R.string.cost_in_rs, totalCost),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview(eventViewModel: EventViewModel = viewModel()) {
    ExpensesScreen(
        eventId = 0,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun TotalCostCardPreview() {
    TotalCostCard(totalCost = 2400)
}

@Preview(showBackground = true)
@Composable
fun ExpenseListPreview(eventViewModel: EventViewModel = viewModel()) {
    ExpenseList(expenseList = getExpenseList())
}

@Preview(showBackground = true)
@Composable
fun ExpenseItemPreview() {
    ExpenseItem(getExpenseList()[0])
}