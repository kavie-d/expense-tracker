package com.example.expensetracker.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.R
import com.example.expensetracker.components.EventList
import com.example.expensetracker.viewModels.EventViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    eventViewModel: EventViewModel = viewModel()
) {
    Box(modifier = modifier.fillMaxSize()) {
        EventList(
            eventList = eventViewModel.events
        )
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 24.dp, bottom = 48.dp)
        ) {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.fab_add)
                )
            }
        }
    }
}