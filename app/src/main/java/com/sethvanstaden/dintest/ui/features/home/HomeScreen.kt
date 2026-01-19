package com.sethvanstaden.dintest.ui.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sethvanstaden.dintest.ui.components.screens.ScreenWithPadding
import com.sethvanstaden.dintest.ui.components.toolbar.LocalTopBarController
import com.sethvanstaden.dintest.ui.components.toolbar.TopBarAction
import com.sethvanstaden.dintest.ui.components.toolbar.TopBarState

@Composable
fun HomeScreen(
    onStartTest: () -> Unit,
    onViewResults: () -> Unit
) {
    val topBar = LocalTopBarController.current

    LaunchedEffect(Unit) {
        topBar.set(
            TopBarState(
                title = "DIN Test",
                showBack = false
            )
        )
    }

    ScreenWithPadding {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Digit-in-Noise Test",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "You’ll hear three digits in background noise. Enter the digits you heard. " +
                        "The noise adapts based on your answers.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onStartTest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Start test")
                    }

                    OutlinedButton(
                        onClick = onViewResults,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("View results")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("How it works", style = MaterialTheme.typography.titleMedium)

                    BulletRow("10 rounds")
                    BulletRow("Difficulty starts at 5 and adapts up/down")
                    BulletRow("Score is the sum of difficulty points per round")
                    BulletRow("Results upload when the test ends")
                }
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = "Tip: use headphones for best results.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun BulletRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
