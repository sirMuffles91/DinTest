package com.sethvanstaden.dintest.ui.features.test

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sethvanstaden.dintest.ui.components.screens.ScreenWithPadding
import com.sethvanstaden.dintest.ui.components.toolbar.LocalTopBarController
import com.sethvanstaden.dintest.ui.components.toolbar.TopBarState
import org.koin.androidx.compose.koinViewModel

@Composable
fun TestScreen(
    onFinished: () -> Unit,
    viewModel: DinTestViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val topBar = LocalTopBarController.current
    var showExitWarning by rememberSaveable { mutableStateOf(false) }

    val canAttemptExit = state.finalScore == null && !state.isUploading

    BackHandler(enabled = canAttemptExit) {
        showExitWarning = true
    }

    DisposableEffect(Unit) { onDispose { viewModel.onExit() } }

    LaunchedEffect(Unit) { viewModel.onScreenShown() }

    LaunchedEffect(Unit) {
        topBar.set(
            TopBarState(
                title = "DIN Test",
                showBack = true,
                onBack = { if (canAttemptExit) showExitWarning = true }
            )
        )
    }

    ScreenWithPadding {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val roundText = if (state.roundIndex == 0) "Starting…" else "Round ${state.roundIndex} of ${state.totalRounds}"

                    Text(
                        text = roundText,
                        style = MaterialTheme.typography.titleMedium
                    )

                    val progress =
                        if (state.totalRounds == 0) 0f else (state.roundIndex.coerceAtLeast(0).toFloat() / state.totalRounds)

                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val statusText = when {
                        state.finalScore != null -> "Finished"
                        state.isUploading -> "Uploading result…"
                        state.roundIndex == 0 -> "Get ready…"
                        state.isPlaying -> "Listening…"
                        state.canSubmit -> "Enter the 3 digits you heard"
                        else -> "Preparing next round…"
                    }

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    OutlinedTextField(
                        value = state.input,
                        onValueChange = viewModel::onInputChanged,
                        label = { Text("Digits") },
                        placeholder = { Text("___") },
                        enabled = state.canSubmit && !state.isUploading,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    )

                    Button(
                        onClick = viewModel::onSubmit,
                        enabled = state.canSubmit && !state.isUploading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isUploading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Uploading…")
                        } else {
                            Text("Submit")
                        }
                    }
                }
            }

            state.error?.let { err ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = err,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        OutlinedButton(
                            onClick = { viewModel.retryFinalize() },
                            enabled = !state.isUploading
                        ) {
                            Text("Retry upload")
                        }
                    }
                }
            }
        }
    }

    state.finalScore?.let { score ->
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Final score") },
            text = { Text("Your score is $score") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissFinalScore()
                        viewModel.onExit()
                        onFinished()
                    }
                ) { Text("Done") }
            }
        )
    }

    if (showExitWarning) {
        AlertDialog(
            onDismissRequest = { showExitWarning = false },
            title = { Text("Exit test?") },
            text = { Text("If you leave now, your test progress will be lost.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitWarning = false
                        viewModel.onExit()
                        onFinished()
                    }
                ) { Text("Exit") }
            },
            dismissButton = {
                TextButton(onClick = { showExitWarning = false }) { Text("Continue") }
            }
        )
    }
}
