package com.sethvanstaden.dintest.ui.components.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinTopAppBar(
    state: TopBarState,
    onNavigateUp: () -> Unit,
) {
    TopAppBar(
        title = { Text(state.title) },
        navigationIcon = {
            if (state.showBack) {
                IconButton(onClick = { (state.onBack ?: onNavigateUp).invoke() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            state.actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(action.icon, contentDescription = action.contentDescription)
                }
            }
        }
    )
}