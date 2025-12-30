package com.app.feature.example.presentation.mvi
     
import android.content.Context
import androidx.compose.runtime.Stable
import com.app.core.ui.mvi.PatternViewModel
import com.app.feature.example.presentation.mvi.ExampleContract.Effect
import com.app.feature.example.presentation.mvi.ExampleContract.Intent
import com.app.feature.example.presentation.mvi.ExampleContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@Stable
@HiltViewModel
class ExampleViewModel @Inject constructor(
    // Inject dependencies here if needed
    @ApplicationContext private val context: Context
) : PatternViewModel<State, Intent, Effect>(
    initialState = State()
) {
    override suspend fun onIntent(intent: Intent) {
        when (intent) {
            // Handle intents here
            else -> {}
        }
    }
}