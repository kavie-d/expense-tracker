package com.example.expensetracker

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensetracker.ui.screens.EventsScreen
import com.example.expensetracker.ui.screens.ExpensesScreen
import com.example.expensetracker.viewModels.AppViewModelProvider
import com.example.expensetracker.viewModels.EventViewModel
import com.example.expensetracker.viewModels.ExpenseViewModel

enum class AppScreen(@StringRes val title: Int) {
    Events(title = R.string.app_screen_title_events),
    Expenses(title = R.string.app_screen_title_expenses)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route?.substringBefore("/") ?: AppScreen.Events.name
    )

    val eventViewModel: EventViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val expenseViewModel: ExpenseViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val totalCost by expenseViewModel.totalCost.collectAsState()

    val scrollBehavior = when (currentScreen) {
        AppScreen.Events -> TopAppBarDefaults.enterAlwaysScrollBehavior()
        AppScreen.Expenses -> TopAppBarDefaults.pinnedScrollBehavior()
    }

    val appBarTitle = if (currentScreen == AppScreen.Expenses) {
        expenseViewModel.eventWithExpenses.collectAsState().value?.event?.eventName ?: "Event Details"
    } else {
        stringResource(currentScreen.title)
    }

    Scaffold(
        topBar = {
            AppBar(
                title = appBarTitle,
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                onBackClick = { navController.popBackStack() },
                scrollBehavior = scrollBehavior,
                onAddExpenseClick = { expenseViewModel.openAddExpenseDialog = true }
            )
        },
        bottomBar = {
            if (currentScreen == AppScreen.Expenses) {
                BottomTotalCostCard(totalCost = totalCost)
            }
        },
        floatingActionButton = {
            if (currentScreen == AppScreen.Events) {
                EventScreenFAB(openAddEventDialog = { eventViewModel.openAddEventDialog = true })
            }
        },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Events.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Events.name) {
                EventsScreen(
                    onEventClick = { eventId -> onEventClick(navController, eventId) },
                    viewModel = eventViewModel
                )
            }
            composable(
                route = AppScreen.Expenses.name + "/{eventId}",
                arguments = listOf(
                    navArgument(name = "eventId") {
                        type = NavType.IntType
                    }
                )
            ) { backstackEntry ->
                ExpensesScreen(
                    eventId = requireNotNull(backstackEntry.arguments?.getInt("eventId")),
                    viewModel = expenseViewModel
                )
            }
        }
    }
}

private fun onEventClick(navController: NavHostController, eventId: Int) {
    navController.navigate("${AppScreen.Expenses.name}/$eventId")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    currentScreen: AppScreen,
    scrollBehavior: TopAppBarScrollBehavior,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    onAddExpenseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LargeTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            if (currentScreen == AppScreen.Expenses) {
                IconButton(onClick = onAddExpenseClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun BottomTotalCostCard(
    totalCost: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            Text(text = stringResource(R.string.string_total), fontWeight = FontWeight.Bold)
            Text(text = stringResource(R.string.cost_in_rs, totalCost.toString()), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EventScreenFAB(
    openAddEventDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = openAddEventDialog,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.fab_add)
        )
    }
}