package com.example.expensetracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.entities.Event
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun EventList(
    eventList: List<Event>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(eventList) { event ->
            EventItem(
                eventName = event.eventName,
                totalCost = event.totalCost
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventListPreview(eventViewModel: EventViewModel = viewModel()) {
    EventList(eventList = eventViewModel.events)
}