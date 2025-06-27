package com.example.expensetracker.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.expensetracker.R
import com.example.expensetracker.entities.Event
import com.example.expensetracker.entities.EventWithExpenses
import com.example.expensetracker.entities.Expense
import com.example.expensetracker.ui.shared.components.InputDialog
import com.example.expensetracker.viewModels.ExpenseViewModel

@Composable
fun ExpensesScreen(
    eventId: Int,
    viewModel: ExpenseViewModel,
    onDismissAddExpenseDialog: () -> Unit,
    onDismissUpdateEventDialog: () -> Unit,
    onDismissDeleteEventDialog: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    openAddExpenseDialog: Boolean = false,
    openUpdateEventDialog: Boolean = false,
    openDeleteEventDialog: Boolean = false,
) {
    LaunchedEffect(eventId) {
        viewModel.loadData(eventId)
    }
    val eventWithExpenses = viewModel.eventWithExpenses.collectAsState().value

    Box(modifier = modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp)) {

        if (eventWithExpenses == null) {
            Box(contentAlignment = Alignment.Center) {
                Text("Error. Event is null.")
            }
            return
        }

        // Expense list
        ExpenseList(expenseList = eventWithExpenses.expenses)

        // Dialogs
        Box {
            var newExpenseName by rememberSaveable { mutableStateOf("") }
            var newExpenseCost by rememberSaveable { mutableStateOf("") }

            when {
                openAddExpenseDialog -> {
                    AddExpenseDialog(
                        expenseName = newExpenseName,
                        expenseCost = newExpenseCost,
                        onExpenseNameChange = { newExpenseName = it },
                        onExpenseCostChange = { newExpenseCost = it },
                        onDismissRequest = {
                            onDismissAddExpenseDialog()
                            newExpenseName = ""
                            newExpenseCost = ""
                        },
                        onConfirmation = {
                            viewModel.addExpense(
                                eventId = eventId,
                                expenseName = newExpenseName,
                                expenseCost = newExpenseCost.toInt()
                            )
                            onDismissAddExpenseDialog()
                            newExpenseName = ""
                            newExpenseCost = ""
                        }
                    )
                }
            }

            var eventName by rememberSaveable { mutableStateOf(eventWithExpenses.event.eventName) }

            when {
                openUpdateEventDialog -> {
                    UpdateEventDialog(
                        onValueChange = { eventName = it },
                        onDismissRequest = onDismissUpdateEventDialog,
                        onConfirmation = {
                            val tempEvent = eventWithExpenses.event
                            tempEvent.eventName = eventName
                            viewModel.updateEvent(tempEvent)
                            onDismissUpdateEventDialog()
                        },
                        value = eventName
                    )
                }
            }

            when {
                openDeleteEventDialog -> {
                    DeleteEventDialog(
                        onDismissRequest = onDismissDeleteEventDialog,
                        onConfirmation = {
                            viewModel.deleteEvent(eventWithExpenses.event)
                            navigateBack()
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun ExpenseList(
    expenseList: List<Expense>?,
    modifier: Modifier = Modifier
) {
    if (expenseList != null) {
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
            Text(text = "No Expenses Added.")
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
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = expense.expenseName)
            Text(text = stringResource(R.string.cost_in_rs, expense.expenseCost))
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
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        border = BorderStroke(1.dp, Color.Gray),
        modifier = modifier.fillMaxWidth()
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
                Text(text = stringResource(R.string.string_total))
                Text(text = stringResource(R.string.cost_in_rs, totalCost))
            }
        }
    }
}

@Composable
fun AddExpenseDialog(
    onExpenseNameChange: (String) -> Unit,
    onExpenseCostChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    expenseName: String = "",
    expenseCost: String = ""
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_add_expense),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_add),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = expenseName,
            onValueChange = onExpenseNameChange,
            placeholder = { Text(stringResource(R.string.txt_field_expense_name)) },
            singleLine = true
        )
        OutlinedTextField(
            value = expenseCost,
            onValueChange = onExpenseCostChange,
            placeholder = { Text(stringResource(R.string.txt_field_expense_cost)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Composable
fun UpdateEventDialog(
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    value: String = ""
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_update_event),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_update),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(stringResource(R.string.txt_field_event_name)) },
            singleLine = true
        )
    }
}

@Composable
fun DeleteEventDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_delete_event),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_delete),
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("Confirm deletion.")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TotalCostCardPreview() {
//    TotalCostCard(totalCost = 2400)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ExpenseListPreview(eventScreenViewModel: EventScreenViewModel = viewModel()) {
//    ExpenseList(expenseList = getExpenseList())
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ExpenseItemPreview() {
//    ExpenseItem(getExpenseList()[0])
//}