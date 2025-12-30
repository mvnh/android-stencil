package com.app.feature.example.presentation.mvi
        
import androidx.compose.runtime.Immutable
import com.app.core.ui.mvi.UiState

object ExampleContract {
        
    @Immutable
    data class State(
        override val isLoading: Boolean = false,
        override val error: Throwable? = null
       // Define your state properties here
    ) : UiState {
        fun toggleLoading(): State = copy(isLoading = !isLoading)
        fun setError(error: Throwable?): State = copy(error = error)
        fun clearError(): State = copy(error = null)
    }
            
    sealed interface Intent {
        // Define your intents here
    }
            
    sealed interface Effect {
        // Define your effects here
    }
}