package com.example.expensetracker.ui.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.expensetracker.R
import com.example.expensetracker.entities.Event
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun EventsScreen(
    onEventClick: (Int) -> Unit,
    viewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val eventList by viewModel.events.collectAsState()
        var openAlertDialog by remember { mutableStateOf(false) }

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
                onClick = { openAlertDialog = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                openAlertDialog -> {
                    AddEventDialog(
                        value = newEventName,
                        onValueChange = { newValue -> newEventName = newValue },
                        onDismissRequest = { openAlertDialog = false },
                        onConfirmation = {
                            openAlertDialog = false
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
        ),
        onClick = { onClick(event.id) },
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
                Text(text = event.eventName, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = stringResource(R.string.cost_in_rs, totalCost), style = MaterialTheme.typography.bodyMedium)
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
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Add Event", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    placeholder = { Text("Event name") },
                    modifier = Modifier.padding(top = 24.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Add")
                    }
                }
            }
        }
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
//fun EventCardPreview() {
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
