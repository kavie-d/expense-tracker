package com.example.expensetracker.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
        var openAddEventDialog by remember { mutableStateOf(false) }

        // Event list
        EventList(
            eventList = eventList,
            onEventClick = onEventClick,
            viewModel = viewModel
        )

        // Floating action button
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 24.dp, bottom = 48.dp)
        ) {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = { openAddEventDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.fab_add)
                )
            }
        }

        // Add event dialog
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            var newEventName by rememberSaveable { mutableStateOf("") }

            when {
                openAddEventDialog -> {
                    AddEventDialog(
                        value = newEventName,
                        onValueChange = { newValue -> newEventName = newValue },
                        onDismissRequest = {
                            openAddEventDialog = false
                            newEventName = ""
                       },
                        onConfirmation = {
                            openAddEventDialog = false
                            viewModel.addEvent(newEventName)
                            newEventName = ""
                        }
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
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EventItem(
    event: Event,
    onClick: (Int) -> Unit,
    viewModel: EventViewModel,
    modifier: Modifier = Modifier,
) {
    val totalCost by viewModel.getTotalCost(event.id).collectAsState()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = { onClick(event.id) },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = event.eventName, style = MaterialTheme.typography.bodyLarge)
                Text(text = stringResource(R.string.cost_in_rs, totalCost))
            }
            Text(
                text = event.getFormattedDate(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
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
            placeholder = { Text(stringResource(R.string.txt_field_event_name)) },
            singleLine = true
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
