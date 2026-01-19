package com.sethvanstaden.dintest.ui.features.results

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sethvanstaden.dintest.domain.model.TestResultUiModel
import com.sethvanstaden.dintest.ui.components.screens.ScreenWithPadding
import com.sethvanstaden.dintest.ui.components.toolbar.LocalTopBarController
import com.sethvanstaden.dintest.ui.components.toolbar.TopBarState

@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val results by viewModel.results.collectAsState()
    val topBar = LocalTopBarController.current

    LaunchedEffect(Unit) {
        topBar.set(
            TopBarState(
                title = "Results",
                showBack = true
            )
        )
    }

    ScreenWithPadding {

        if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No saved results yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = results,
                    key = { it.id }
                ) { item ->
                    ResultRow(item)
                }
            }
        }
    }
}

@Composable
private fun ResultRow(item: TestResultUiModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Score: ${item.score}", style = MaterialTheme.typography.titleMedium)
            Text(
                "Correct: ${item.correctLabel}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Difficulty path: ${item.difficultyPathLabel}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Date: ${item.dateLabel}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}