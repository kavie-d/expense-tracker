package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.entities.Expense
import com.example.expensetracker.ui.shared.components.InputDialog
import com.example.expensetracker.viewModels.ExpenseViewModel

@Composable
fun ExpensesScreen(
    eventId: Int,
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
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
        ExpenseList(
            expenseList = eventWithExpenses.expenses,
            onUpdateClick = { viewModel.openUpdateExpenseDialog = true },
            onDeleteClick = { viewModel.openDeleteExpenseDialog = true },
            setSelectedExpense = { viewModel.setSelectedExpense(it) }
        )

        // Dialogs
        Box {
            when {
                viewModel.openAddExpenseDialog -> {
                    AddExpenseDialog(
                        expenseName = viewModel.newExpenseName,
                        expenseCost = viewModel.newExpenseCost,
                        onExpenseNameChange = { viewModel.newExpenseName = it },
                        onExpenseCostChange = { viewModel.newExpenseCost = it },
                        onDismissRequest = { viewModel.onAddExpenseDialogDismiss() },
                        onConfirmation = { viewModel.onAddExpenseDialogConfirm(eventId) }
                    )
                }

                viewModel.openUpdateExpenseDialog -> {
                    viewModel.selectedExpense?.let {
                        UpdateExpenseDialog(
                            expenseName = it.expenseName,
                            expenseCost = it.expenseCost.toString(),
                            onExpenseNameChange = { name -> viewModel.onSelectedExpenseNameChange(name) },
                            onExpenseCostChange = { cost -> viewModel.onSelectedExpenseCostChange(cost) },
                            onDismissRequest = { viewModel.onUpdateExpenseDialogDismiss() },
                            onConfirmation = { viewModel.onUpdateExpenseDialogConfirm() }
                        )
                    }
                }

                viewModel.openDeleteExpenseDialog -> {
                    DeleteExpenseDialog(
                        onDismissRequest = { viewModel.onDeleteExpenseDialogDismiss() },
                        onConfirmation = { viewModel.onDeleteExpenseDialogConfirm() }
                    )
                }
            }
        }
    }

}

@Composable
fun ExpenseList(
    expenseList: List<Expense>?,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    setSelectedExpense: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    if (expenseList != null) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(expenseList) { expense ->
                ExpenseItem(
                    expense = expense,
                    onUpdateClick = onUpdateClick,
                    onDeleteClick = onDeleteClick,
                    setSelectedExpense = setSelectedExpense
                )
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
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    setSelectedExpense: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Expense name
            Text(text = expense.expenseName)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Expense cost
                Text(text = stringResource(R.string.cost_in_rs, expense.expenseCost))

                // Menu
                Box {
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.content_description_more_options))
                    }
                    when {
                        menuExpanded -> {
                            setSelectedExpense(expense)
                            ExpenseItemMenu(
                                expanded = true,
                                onDismissRequest = { menuExpanded = false },
                                onUpdateClick = onUpdateClick,
                                onDeleteClick = onDeleteClick,
                            )
                        }
                    }
                }
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
fun UpdateExpenseDialog(
    onExpenseNameChange: (String) -> Unit,
    onExpenseCostChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    expenseName: String,
    expenseCost: String
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_update_expense),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_update),
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
fun DeleteExpenseDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_delete_expense),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_delete),
        modifier = modifier
    ) {
        Text("Are you sure?")
    }
}

@Composable
fun ExpenseItemMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = { Text("Update") },
            leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
            onClick = onUpdateClick
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
            onClick = onDeleteClick
        )
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