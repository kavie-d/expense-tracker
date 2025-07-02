package com.example.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.expensetracker.R
import com.example.expensetracker.entities.Event
import com.example.expensetracker.ui.shared.components.InputDialog
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun EventsScreen(
    onEventClick: (Int) -> Unit,
    viewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        val eventList by viewModel.events.collectAsState()

        // Event list
        EventList(
            eventList = eventList,
            onEventClick = onEventClick,
            setSelectedEvent = { viewModel.setSelectedEvent(it) },
            onUpdateClick = { viewModel.openUpdateEventDialog = true },
            onDeleteClick = { viewModel.openDeleteEventDialog = true },
            viewModel = viewModel
        )

        // Add event dialog
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                viewModel.openAddEventDialog -> {
                    AddEventDialog(
                        value = viewModel.newEventName,
                        onValueChange = { viewModel.newEventName = it },
                        onDismissRequest = { viewModel.onAddEventDialogDismiss() },
                        onConfirmation = { viewModel.onAddEventDialogConfirm() },
                        isEventNameError = viewModel.isAddEventNameError
                    )
                }

                viewModel.openUpdateEventDialog -> {
                    viewModel.selectedEvent?.let {
                        UpdateEventDialog(
                            value = it.eventName,
                            onValueChange = { name -> viewModel.onSelectedEventNameChange(name) },
                            onDismissRequest = { viewModel.onUpdateEventDialogDismiss() },
                            onConfirmation = { viewModel.onUpdateEventDialogConfirm() },
                            isEventNameError = viewModel.isUpdateEventNameError
                        )
                    }
                }

                viewModel.openDeleteEventDialog -> {
                    DeleteEventDialog(
                        onDismissRequest = { viewModel.onDeleteEventDialogDismiss() },
                        onConfirmation = { viewModel.onDeleteEventDialogConfirm() }
                    )
                }
            }
        }
    }
}

@Composable
fun EventList(
    eventList: List<Event>,
    onEventClick: (Int) -> Unit,
    setSelectedEvent: (Event) -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    viewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 24.dp),
        modifier = modifier
    ) {
        items(eventList) { event ->
            EventItem(
                event = event,
                onClick = onEventClick,
                setSelectedEvent = setSelectedEvent,
                onUpdateClick = onUpdateClick,
                onDeleteClick = onDeleteClick,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: (Int) -> Unit,
    setSelectedEvent: (Event) -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    viewModel: EventViewModel,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val totalCost by viewModel.getTotalCost(event.id).collectAsState()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = { onClick(event.id) },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Event name
                Text(text = event.eventName, style = MaterialTheme.typography.bodyLarge)

                // Date
                Text(
                    text = event.getFormattedDate(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total cost
                Text(text = stringResource(R.string.cost_in_rs, totalCost))

                // Menu
                Box {
                    IconButton(onClick = { menuExpanded = !menuExpanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.content_description_more_options))
                    }
                    when {
                        menuExpanded -> {
                            setSelectedEvent(event)
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
fun AddEventDialog(
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    value: String = "",
    isEventNameError: Boolean = false
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_add_event),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_add),
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(if (!isEventNameError) R.string.txt_field_event_name else R.string.txt_field_event_name_error)) },
            singleLine = true,
            isError = isEventNameError
        )
    }
}

@Composable
fun UpdateEventDialog(
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
    value: String = "",
    isEventNameError: Boolean = false
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
            label = { Text(stringResource(if (!isEventNameError) R.string.txt_field_new_event_name else R.string.txt_field_event_name_error)) },
            singleLine = true,
            isError = isEventNameError
        )
    }
}

@Composable
fun DeleteEventDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    InputDialog(
        dialogTitle = stringResource(R.string.txt_delete_event),
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        confirmationText = stringResource(R.string.btn_txt_delete),
        modifier = modifier
    ) {
        Text("Are you sure?")
    }
}

@Composable
fun EventItemMenu(
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
//fun EventListPreview(eventScreenViewModel: EventScreenViewModel = viewModel()) {
//    EventList(
//        eventList = listOf(),
//        onEventClick = {},
//        viewModel = viewModel()
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun EventItemPreview() {
//    EventItem(
//        event = Event(eventName = "Event 1"),
//        onClick = {},
//        viewModel = viewModel()
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AddEventDialogPreview() {
//    AddEventDialog(
//        value = "",
//        onValueChange = {},
//        onDismissRequest = {},
//        onConfirmation = {}
//    )
//}
