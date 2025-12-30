package com.app.core.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.core.R
import com.app.core.ui.mvi.PatternViewModel
import com.app.core.ui.mvi.UiState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow

@Stable
fun interface IntentProcessor<I> {
    operator fun invoke(intent: I)
}

/**
 * A generic, reusable composable that provides a foundational structure for screens following the MVI (Model-View-Intent) pattern.
 * It handles common UI concerns such as loading and error states, snackbar messages, and wiring up the ViewModel.
 *
 * This composable uses a [Scaffold] to provide a standard layout structure, including top bar, bottom bar, and floating action button.
 * It automatically observes the [UiState] from the provided [PatternViewModel] and displays:
 * - A loading overlay when `state.isLoading` is true.
 * - An error overlay when `state.error` is not null.
 * - The main `content` when the state is stable.
 *
 * It also collects one-time side effects ([E]) from the ViewModel and allows for custom handling via the [onEffect] lambda.
 *
 * @param S The type of the UI state, which must extend [UiState].
 * @param I The type of the user intents/actions.
 * @param E The type of the one-time side effects.
 *
 * @param viewModel The instance of [PatternViewModel] that manages the screen's state and logic.
 * @param modifier The [Modifier] to be applied to the root [Scaffold] composable.
 * @param fab A composable lambda for the floating action button. It receives a function to process an [I] (intent).
 * @param topBar A composable lambda for the top app bar. It receives the current state [S] and a function to process an [I].
 * @param bottomBar A composable lambda for the bottom app bar. It receives the current state [S] and a function to process an [I].
 * @param onBackPressed An optional lambda to be invoked when the system back button is pressed. If provided, a [BackHandler] will be set up.
 */
@Composable
fun <S : UiState, I, E> BaseScreen(
    viewModel: PatternViewModel<S, I, E>,
    modifier: Modifier = Modifier,
    fab: @Composable (IntentProcessor<I>) -> Unit = { _ -> },
    topBar: @Composable (S, IntentProcessor<I>) -> Unit = { _, _ -> },
    bottomBar: @Composable (S, IntentProcessor<I>) -> Unit = { _, _ -> },
    onBackPressed: (() -> Unit)? = null,
    snackbarHost: @Composable (SnackbarHostState) -> Unit = { state ->
        SnackbarHost(hostState = state)
    },
    onEffect: (E, SnackbarHostState) -> Unit = { _, _ -> },
    content: @Composable BoxScope.(S, IntentProcessor<I>) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    onBackPressed?.let { handler ->
        BackHandler(onBack = handler)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect
            .receiveAsFlow()
            .catch { throwable -> throwable.printStackTrace() }
            .collect { effect ->
                onEffect(effect, snackbarHostState)
            }
    }

    val intentProcessor = remember(viewModel) {
        IntentProcessor<I> { intent ->
            viewModel.processIntent(intent)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { topBar(state, intentProcessor) },
        floatingActionButton = { fab(intentProcessor) },
        bottomBar = { bottomBar(state, intentProcessor) },
        snackbarHost = { snackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content(state, intentProcessor)

            when {
                state.isLoading -> {
                    LoadingOverlay()
                }
                state.error != null -> {
                    ErrorOverlay(
                        error = state.error!!,
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorOverlay(
    error: Throwable,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.error_occurred),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = error.localizedMessage ?: stringResource(R.string.unknown_error),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
            if (onRetry != null) {
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }
    }
}